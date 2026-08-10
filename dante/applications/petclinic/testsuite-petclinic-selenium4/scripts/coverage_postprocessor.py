#!/usr/bin/env python3
"""Build three coverage metrics from the two Selenium/CDP suite runs.

Inputs
------
modern-raw-run/suite-coverage.json
    detailed=true, callCount=true, all accepted bundles.

dante-compatible-run/test-raw-ranges.json
    detailed=false, callCount=false, per-test function ranges.

Outputs
-------
coverage-summary.json / .csv / .md
raw-bundle-coverage.json
modern-application-coverage.json / .csv
dante-compatible-coverage.json

The DANTE-compatible calculation ports the behavior visible in the supplied
DANTE classes (CodeCoverage, ScriptRangeCoverage, CoverageRangeWrapper,
PercentageCovered and SourceMapParser). The source-map phase reproduces
SourceMapParser's source-name filtering and its per-source min/max generated-line
ranges, including the historical empty-exclusion behavior.
"""

from __future__ import annotations

import argparse
import csv
import json
import re
import sys
import urllib.request
from collections import defaultdict
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Iterable, Iterator, Sequence


BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
BASE64_TO_INT = {char: index for index, char in enumerate(BASE64)}


@dataclass(frozen=True)
class Interval:
    start: int
    end: int  # end-exclusive

    def __post_init__(self) -> None:
        if self.end < self.start:
            raise ValueError(f"Invalid interval: {self.start}..{self.end}")


@dataclass(frozen=True)
class MappingSegment:
    generated_line: int
    generated_column: int
    source_index: int | None
    original_line: int | None
    original_column: int | None


@dataclass
class SourceMapData:
    raw_sources: list[str]
    sources: list[str]
    sources_content: list[str | None]
    segments_by_line: dict[int, list[MappingSegment]]


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--coverage-root",
        required=True,
        help="Directory containing modern-raw-run and dante-compatible-run",
    )
    parser.add_argument(
        "--sourcemap-url",
        default="http://localhost:3000/main.js.map",
    )
    parser.add_argument(
        "--source-url",
        default="",
        help="Optional main.js URL/path. If omitted, it is read from reports.",
    )
    parser.add_argument(
        "--main-script-regex",
        default=r"(?:^|/)main\.js(?:\?.*)?$",
    )
    parser.add_argument(
        "--app-source-include-regex",
        default=r"(?:^|/)(?:src|app)/",
    )
    parser.add_argument(
        "--app-source-exclude-regex",
        default=r"(?:^|/)(?:node_modules|vendor|webpack)(?:/|$)",
    )
    parser.add_argument(
        "--dante-src-code-folder",
        default="src",
        help="Exact DANTE SourceMapParser src_code_folder value.",
    )
    parser.add_argument(
        "--dante-src-code-files-to-exclude",
        default="",
        help=(
            "Exact colon-separated DANTE src_code_files_to_exclude value. "
            "An empty value is intentionally preserved as one empty token, "
            "matching Java String.split and the supplied SourceMapParser."
        ),
    )
    parser.add_argument(
        "--dante-empty-exclusion-behavior",
        choices=("java", "ignore_empty"),
        default="java",
        help=(
            "java reproduces the supplied SourceMapParser literally; "
            "ignore_empty removes empty exclusion tokens and represents the "
            "likely intended behavior. Academic comparison should use java "
            "unless the historical run is proven to have used a patched parser."
        ),
    )
    parser.add_argument(
        "--output-dir",
        default="",
    )
    return parser.parse_args()


def load_json(path: Path) -> dict[str, Any]:
    return json.loads(path.read_text(encoding="utf-8"))


def fetch_text(location: str) -> str:
    if location.startswith("file://"):
        return Path(location[7:]).read_text(encoding="utf-8")

    path = Path(location)
    if path.exists():
        return path.read_text(encoding="utf-8")

    with urllib.request.urlopen(location, timeout=30) as response:
        raw = response.read()
        charset = response.headers.get_content_charset() or "utf-8"
        return raw.decode(charset, errors="replace")


def decode_vlq(segment: str) -> list[int]:
    values: list[int] = []
    value = 0
    shift = 0

    for char in segment:
        if char not in BASE64_TO_INT:
            raise ValueError(f"Invalid base64 VLQ character: {char!r}")

        digit = BASE64_TO_INT[char]
        continuation = digit & 32
        digit &= 31
        value += digit << shift

        if continuation:
            shift += 5
            continue

        negative = value & 1
        decoded = value >> 1
        values.append(-decoded if negative else decoded)
        value = 0
        shift = 0

    if shift != 0:
        raise ValueError(f"Incomplete VLQ segment: {segment!r}")

    return values


def normalize_source_name(source_root: str, source: str) -> str:
    source = source.replace("\\", "/")
    source_root = source_root.replace("\\", "/")

    if source.startswith("webpack:///"):
        source = source[len("webpack:///") :]
    elif source.startswith("webpack://"):
        source = source[len("webpack://") :]

    source = re.sub(r"^\./", "", source)

    if source_root and not re.match(r"^[a-zA-Z]+://", source):
        root = source_root.rstrip("/")
        source = f"{root}/{source.lstrip('/')}"

    return source


def parse_source_map(text: str) -> SourceMapData:
    raw = json.loads(text)
    sources_raw = [str(item) for item in raw.get("sources", [])]
    source_root = str(raw.get("sourceRoot", "") or "")
    sources = [normalize_source_name(source_root, item) for item in sources_raw]

    raw_contents = raw.get("sourcesContent") or []
    sources_content: list[str | None] = []
    for index in range(len(sources)):
        if index < len(raw_contents) and raw_contents[index] is not None:
            sources_content.append(str(raw_contents[index]))
        else:
            sources_content.append(None)

    mappings = str(raw.get("mappings", "") or "")
    segments_by_line: dict[int, list[MappingSegment]] = defaultdict(list)

    previous_source = 0
    previous_original_line = 0
    previous_original_column = 0

    for generated_line, encoded_line in enumerate(mappings.split(";")):
        previous_generated_column = 0

        if not encoded_line:
            continue

        for encoded_segment in encoded_line.split(","):
            if not encoded_segment:
                continue

            values = decode_vlq(encoded_segment)
            if not values:
                continue

            previous_generated_column += values[0]

            source_index: int | None = None
            original_line: int | None = None
            original_column: int | None = None

            if len(values) >= 4:
                previous_source += values[1]
                previous_original_line += values[2]
                previous_original_column += values[3]
                source_index = previous_source
                original_line = previous_original_line
                original_column = previous_original_column

            segments_by_line[generated_line].append(
                MappingSegment(
                    generated_line=generated_line,
                    generated_column=previous_generated_column,
                    source_index=source_index,
                    original_line=original_line,
                    original_column=original_column,
                )
            )

    return SourceMapData(
        raw_sources=sources_raw,
        sources=sources,
        sources_content=sources_content,
        segments_by_line=dict(segments_by_line),
    )


def merge_intervals(intervals: Iterable[Interval]) -> list[Interval]:
    sorted_intervals = sorted(intervals, key=lambda item: (item.start, item.end))
    if not sorted_intervals:
        return []

    merged: list[Interval] = []
    current_start = sorted_intervals[0].start
    current_end = sorted_intervals[0].end

    for interval in sorted_intervals[1:]:
        if interval.start <= current_end:
            current_end = max(current_end, interval.end)
        else:
            merged.append(Interval(current_start, current_end))
            current_start = interval.start
            current_end = interval.end

    merged.append(Interval(current_start, current_end))
    return merged


def interval_length(intervals: Iterable[Interval]) -> int:
    return sum(interval.end - interval.start for interval in merge_intervals(intervals))


def intersection(a: Interval, b: Interval) -> Interval | None:
    start = max(a.start, b.start)
    end = min(a.end, b.end)
    return Interval(start, end) if end > start else None


def intersects_any(interval: Interval, candidates: Sequence[Interval]) -> bool:
    for candidate in candidates:
        if candidate.end <= interval.start:
            continue
        if candidate.start >= interval.end:
            break
        if candidate.start < interval.end and candidate.end > interval.start:
            return True
    return False


def intersect_sets(left: Sequence[Interval], right: Sequence[Interval]) -> list[Interval]:
    left_m = merge_intervals(left)
    right_m = merge_intervals(right)
    result: list[Interval] = []
    i = 0
    j = 0

    while i < len(left_m) and j < len(right_m):
        item = intersection(left_m[i], right_m[j])
        if item is not None:
            result.append(item)

        if left_m[i].end <= right_m[j].end:
            i += 1
        else:
            j += 1

    return merge_intervals(result)


def parse_intervals(raw: Sequence[dict[str, Any]]) -> list[Interval]:
    return merge_intervals(
        Interval(int(item["startOffset"]), int(item["endOffset"]))
        for item in raw
        if int(item["endOffset"]) > int(item["startOffset"])
    )


def line_offsets(source: str) -> tuple[list[str], list[int], list[int]]:
    lines = source.splitlines(keepends=True)
    if not lines:
        lines = [""]

    starts: list[int] = []
    ends: list[int] = []
    cursor = 0

    for line in lines:
        starts.append(cursor)
        content = line.rstrip("\r\n")
        ends.append(cursor + len(content))
        cursor += len(line)

    return lines, starts, ends


def find_script(report: dict[str, Any], pattern: re.Pattern[str]) -> dict[str, Any]:
    matches = [script for script in report.get("scripts", []) if pattern.search(str(script.get("url", "")))]
    if not matches:
        raise RuntimeError(f"No script matched {pattern.pattern!r}")
    if len(matches) > 1:
        matches.sort(key=lambda item: len(str(item.get("url", ""))))
    return matches[0]


def application_source_predicate(
    include: re.Pattern[str],
    exclude: re.Pattern[str],
):
    def predicate(source: str) -> bool:
        normalized = source.replace("\\", "/")
        return bool(include.search(normalized)) and not bool(exclude.search(normalized))

    return predicate


def iter_generated_mapping_intervals(
    source: str,
    source_map: SourceMapData,
) -> Iterator[tuple[Interval, MappingSegment]]:
    _lines, starts, ends = line_offsets(source)

    for line_number, segments in source_map.segments_by_line.items():
        if line_number >= len(starts):
            continue

        ordered = sorted(segments, key=lambda item: item.generated_column)
        line_start = starts[line_number]
        line_end = ends[line_number]

        for index, segment in enumerate(ordered):
            start = min(line_start + segment.generated_column, line_end)
            if index + 1 < len(ordered):
                end_column = ordered[index + 1].generated_column
                end = min(line_start + end_column, line_end)
            else:
                end = line_end

            if end > start:
                yield Interval(start, end), segment


def build_modern_application_report(
    modern_report: dict[str, Any],
    source: str,
    source_map: SourceMapData,
    main_pattern: re.Pattern[str],
    source_is_application,
) -> dict[str, Any]:
    main_script = find_script(modern_report, main_pattern)
    known = parse_intervals(main_script.get("knownRanges", []))
    covered = parse_intervals(main_script.get("coveredRanges", []))

    mapped_application_intervals: list[Interval] = []
    per_file_known: dict[str, set[int]] = defaultdict(set)
    per_file_covered: dict[str, set[int]] = defaultdict(set)

    for generated_interval, segment in iter_generated_mapping_intervals(source, source_map):
        if segment.source_index is None or segment.original_line is None:
            continue
        if segment.source_index < 0 or segment.source_index >= len(source_map.sources):
            continue

        source_name = source_map.sources[segment.source_index]
        if not source_is_application(source_name):
            continue

        mapped_application_intervals.append(generated_interval)

        if intersects_any(generated_interval, known):
            per_file_known[source_name].add(segment.original_line + 1)
        if intersects_any(generated_interval, covered):
            per_file_covered[source_name].add(segment.original_line + 1)

    mapped_application_intervals = merge_intervals(mapped_application_intervals)
    app_known_generated = intersect_sets(mapped_application_intervals, known)
    app_covered_generated = intersect_sets(mapped_application_intervals, covered)

    files: list[dict[str, Any]] = []
    total_lines = 0
    covered_lines = 0

    all_files = sorted(per_file_known)
    for source_name in all_files:
        known_lines = per_file_known[source_name]
        covered_source_lines = per_file_covered.get(source_name, set()) & known_lines
        total = len(known_lines)
        covered_count = len(covered_source_lines)
        total_lines += total
        covered_lines += covered_count
        files.append(
            {
                "source": source_name,
                "mappedLines": total,
                "coveredLines": covered_count,
                "coveragePercent": round(covered_count * 100.0 / total, 6) if total else 0.0,
            }
        )

    generated_known_bytes = interval_length(app_known_generated)
    generated_covered_bytes = interval_length(app_covered_generated)

    return {
        "metric": "source_map_mapped_application_line_coverage",
        "primaryMetric": True,
        "scriptUrl": main_script.get("url", ""),
        "mappedSourceFiles": len(files),
        "mappedLines": total_lines,
        "coveredLines": covered_lines,
        "coveragePercent": round(covered_lines * 100.0 / total_lines, 6) if total_lines else 0.0,
        "generatedApplicationKnownBytes": generated_known_bytes,
        "generatedApplicationCoveredBytes": generated_covered_bytes,
        "generatedApplicationByteCoveragePercent": round(
            generated_covered_bytes * 100.0 / generated_known_bytes,
            6,
        ) if generated_known_bytes else 0.0,
        "lineCoverageSemantics": (
            "A mapped original line is covered when at least one generated source-map "
            "segment for that line intersects a covered V8 detailed range. The denominator "
            "contains application-owned original lines represented by known generated ranges."
        ),
        "files": files,
    }


def java_split_newlines(text: str) -> list[str]:
    # Java String.split("\\n") with limit=0 removes trailing empty elements.
    parts = text.split("\n")
    while len(parts) > 1 and parts[-1] == "":
        parts.pop()
    return parts


def dante_source_map_line_selection(
    source_map: SourceMapData,
    src_code_folder: str,
    raw_exclusion_value: str,
    empty_exclusion_behavior: str,
) -> tuple[set[int], dict[str, Any]]:
    """Port the supplied DANTE SourceMapParser.java literally.

    Important historical details:
    - Atlassian SourceMap Mapping.getGeneratedLine() is zero-based.
    - Source names only have the literal substring ``webpack:///`` removed.
    - A source is accepted only when it starts with ``./<src_code_folder>``.
    - For every accepted source, one inclusive min/max generated-line range is
      produced; gaps between mapped lines are therefore included.
    - ScriptRangeCoverage later compares those range values with ``i + 1``.
      We preserve that off-by-one interaction by returning the zero-based range
      integers unchanged.
    - Java ``"".split(":")`` yields one empty token. Because every string
      contains the empty string, an empty exclusion property rejects every
      source. The resulting empty line-range set makes ScriptRangeCoverage
      consider all main.js lines. ``empty_exclusion_behavior=java`` preserves
      this behavior exactly.
    """
    if not src_code_folder:
        raise ValueError("DANTE src_code_folder must not be empty")

    exclusions = raw_exclusion_value.split(":")
    if empty_exclusion_behavior == "ignore_empty":
        exclusions = [item for item in exclusions if item]

    generated_lines_by_source: dict[str, set[int]] = defaultdict(set)

    for line_number, segments in source_map.segments_by_line.items():
        for segment in segments:
            if segment.source_index is None:
                continue
            if not (0 <= segment.source_index < len(source_map.raw_sources)):
                continue

            source_name = source_map.raw_sources[segment.source_index]
            source_name = source_name.replace("webpack:///", "")

            accepted = (
                source_name.startswith("./" + src_code_folder)
                and not any(token in source_name for token in exclusions)
                and source_name.endswith((".js", ".jsx", ".ts"))
            )
            if accepted:
                generated_lines_by_source[source_name].add(line_number)

    ranges: set[tuple[int, int]] = set()
    for lines in generated_lines_by_source.values():
        if lines:
            ranges.add((min(lines), max(lines)))

    selected: set[int] = set()
    for start, end in ranges:
        selected.update(range(start, end + 1))

    diagnostics = {
        "srcCodeFolder": src_code_folder,
        "rawSrcCodeFilesToExclude": raw_exclusion_value,
        "parsedExclusionTokens": exclusions,
        "emptyExclusionBehavior": empty_exclusion_behavior,
        "matchedSourceFiles": len(generated_lines_by_source),
        "lineRanges": [
            {"start": start, "end": end}
            for start, end in sorted(ranges)
        ],
        "flattenedLineRangeUnits": len(selected),
        "emptyLineRangesMeanAllMainJsLines": not bool(selected),
        "generatedLineIndexing": (
            "Atlassian mapping lines are zero-based; values are intentionally "
            "compared unchanged with ScriptRangeCoverage's one-based i+1 line "
            "numbers to reproduce the supplied implementation."
        ),
    }
    return selected, diagnostics


def build_dante_allowed_characters(
    source: str,
    selected_lines: set[int],
) -> tuple[set[int], int, int]:
    lines = java_split_newlines(source)
    allowed: set[int] = set()
    selected_line_count = 0
    number_of_characters = -1
    start_offset = 1

    for line_number, line in enumerate(lines, start=1):
        end_offset = start_offset + len(line)
        if not selected_lines or line_number in selected_lines:
            characters = set(range(start_offset, end_offset + 1))
            allowed.update(characters)
            number_of_characters += len(characters)
            selected_line_count += 1
        start_offset = end_offset + 1

    if number_of_characters == -1:
        number_of_characters = len(source)

    return allowed, number_of_characters, selected_line_count


def dante_can_add(allowed: set[int], start: int, end: int) -> bool:
    if start == end:
        inner = [start]
    else:
        inner = range(start + 1, end)
    return all(position in allowed for position in inner)


def safe_dante_substring(source: str, start: int, end: int) -> str:
    # Port of the three Java substring attempts in ScriptRangeCoverage.
    for candidate_end in (end + 1, end, end - 1):
        if 0 <= start <= candidate_end <= len(source):
            return source[start:candidate_end]
    if start < 0 or start > len(source):
        return ""
    return source[start:max(start, min(end, len(source)))]


def dante_overlapping_ranges(
    source: str,
    start: int,
    end: int,
    count: int,
) -> list[tuple[int, int, int]]:
    function_text = safe_dante_substring(source, start, end)

    indexes: list[int] = []
    cursor = 0
    while True:
        index = function_text.find("function", cursor)
        if index == -1:
            break
        indexes.append(index)
        cursor = index + len("function")

    if not indexes:
        return []

    indexes.pop(0)
    nested_characters: set[int] = set()

    for snippet_index in indexes:
        snippet = function_text[snippet_index:]
        close_index = -1
        open_braces: list[int] = []
        close_braces: list[int] = []

        for index, char in enumerate(snippet):
            if char == "{":
                open_braces.append(index)
            elif char == "}":
                close_braces.append(index)
                if len(open_braces) == 1:
                    close_index = index
                    break

        if close_index == -1:
            close_index = close_braces[-1] if close_braces else len(snippet)

        nested_start = snippet_index
        nested_end = snippet_index + close_index + 1
        nested_characters.update(
            range(start + nested_start, start + nested_end + 1)
        )

    parent_characters = set(range(start, end + 1))
    remaining = sorted(parent_characters - nested_characters)
    if not remaining:
        return []

    result: list[tuple[int, int, int]] = []
    range_start = -1

    for index in range(len(remaining) - 1):
        first = remaining[index]
        if range_start == -1:
            range_start = first
        second = remaining[index + 1]
        if abs(first - second) > 1:
            result.append((range_start, first, 1))
            range_start = -1

    result.append((range_start, remaining[-1], 1))
    return result


def build_dante_report(
    raw_tests: dict[str, Any],
    source: str,
    source_map: SourceMapData,
    main_pattern: re.Pattern[str],
    dante_src_code_folder: str,
    dante_src_code_files_to_exclude: str,
    dante_empty_exclusion_behavior: str,
) -> dict[str, Any]:
    selected_lines, source_map_diagnostics = dante_source_map_line_selection(
        source_map,
        dante_src_code_folder,
        dante_src_code_files_to_exclude,
        dante_empty_exclusion_behavior,
    )
    allowed, denominator, selected_line_count = build_dante_allowed_characters(
        source,
        selected_lines,
    )

    covered_units: set[int] = set()
    successful_tests = 0
    accepted_ranges = 0
    rejected_ranges = 0
    whole_file_ranges = 0

    for test in raw_tests.get("tests", []):
        if str(test.get("status", "")) != "PASSED":
            continue
        successful_tests += 1

        for script in test.get("scripts", []):
            url = str(script.get("url", ""))
            if not main_pattern.search(url):
                continue

            for raw_range in script.get("ranges", []):
                start = int(raw_range.get("startOffset", 0))
                end = int(raw_range.get("endOffset", 0))
                count = int(raw_range.get("count", 0))

                if count <= 0 or abs(end - start) == 0:
                    rejected_ranges += 1
                    continue

                diff = abs((end - start) - len(source))
                if diff in (0, 1):
                    whole_file_ranges += 1
                    continue

                if not dante_can_add(allowed, start, end):
                    rejected_ranges += 1
                    continue

                replacements = dante_overlapping_ranges(
                    source,
                    start,
                    end,
                    count,
                )

                ranges_to_add = replacements or [(start, end, count)]
                accepted_ranges += len(ranges_to_add)

                for accepted_start, accepted_end, _accepted_count in ranges_to_add:
                    covered_units.update(range(accepted_start, accepted_end + 1))

    covered_units = {unit for unit in covered_units if unit > 0}
    covered_count = len(covered_units)
    percentage = covered_count * 100.0 / denominator if denominator else 0.0

    return {
        "metric": "dante_compatible_source_map_filtered_main_js_character_coverage",
        "scriptSelection": main_pattern.pattern,
        "sourceMapSelectedGeneratedLines": selected_line_count,
        "sourceMapParserDiagnostics": source_map_diagnostics,
        "totalCoverageUnits": denominator,
        "coveredCoverageUnits": covered_count,
        "coveragePercent": round(percentage, 4),
        "successfulTestsInUnion": successful_tests,
        "acceptedRanges": accepted_ranges,
        "rejectedRanges": rejected_ranges,
        "wholeFileRangesExcluded": whole_file_ranges,
        "historicalSemantics": {
            "callCount": False,
            "detailed": False,
            "rangeEnd": "inclusive after CoverageRangeWrapper.flattenRange()",
            "zeroAndWholeFileRangesExcluded": True,
            "nestedFunctionHeuristic": "ported from ScriptRangeCoverage.getOverlappingCoverageObjects",
            "denominator": "selected generated main.js line character ranges, matching PercentageCovered",
        },
        "compatibilityNote": (
            "SourceMapParser.java is now ported explicitly: source-name filtering, "
            "per-source inclusive min/max generated-line ranges, zero-based generated "
            "line values, and the supplied implementation's empty-exclusion behavior "
            "are represented. With an empty src_code_files_to_exclude value and "
            "emptyExclusionBehavior=java, no source file matches and DANTE falls back "
            "to all main.js lines because ScriptRangeCoverage treats an empty line-range "
            "set as unfiltered. Use ignore_empty only when reproducing a known patched "
            "historical runtime."
        ),
    }


def write_json(path: Path, data: Any) -> None:
    path.write_text(
        json.dumps(data, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )


def write_modern_csv(path: Path, modern: dict[str, Any]) -> None:
    with path.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(["source", "mapped_lines", "covered_lines", "coverage_percent"])
        for item in modern.get("files", []):
            writer.writerow(
                [
                    item["source"],
                    item["mappedLines"],
                    item["coveredLines"],
                    f'{item["coveragePercent"]:.6f}',
                ]
            )


def write_summary_csv(path: Path, summary: dict[str, Any]) -> None:
    with path.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(["metric", "coverage_percent", "covered", "total", "unit"])
        writer.writerow(
            [
                "raw_bundle_coverage",
                f'{summary["rawBundleCoverage"]["coveragePercent"]:.6f}',
                summary["rawBundleCoverage"]["coveredBytes"],
                summary["rawBundleCoverage"]["knownBytes"],
                "generated_bundle_bytes",
            ]
        )
        writer.writerow(
            [
                "modern_application_coverage",
                f'{summary["modernApplicationCoverage"]["coveragePercent"]:.6f}',
                summary["modernApplicationCoverage"]["coveredLines"],
                summary["modernApplicationCoverage"]["mappedLines"],
                "source_mapped_lines",
            ]
        )
        writer.writerow(
            [
                "dante_compatible_coverage",
                f'{summary["danteCompatibleCoverage"]["coveragePercent"]:.4f}',
                summary["danteCompatibleCoverage"]["coveredCoverageUnits"],
                summary["danteCompatibleCoverage"]["totalCoverageUnits"],
                "historical_character_units",
            ]
        )


def write_summary_markdown(path: Path, summary: dict[str, Any]) -> None:
    raw = summary["rawBundleCoverage"]
    modern = summary["modernApplicationCoverage"]
    dante = summary["danteCompatibleCoverage"]

    text = f"""# Three-Metric Coverage Summary

| Metric | Result | Covered / total | Primary use |
|---|---:|---:|---|
| DANTE-compatible coverage | **{dante['coveragePercent']:.4f}%** | {dante['coveredCoverageUnits']:,} / {dante['totalCoverageUnits']:,} character units | Academic comparison with DANTE |
| Modern application coverage | **{modern['coveragePercent']:.6f}%** | {modern['coveredLines']:,} / {modern['mappedLines']:,} source-mapped lines | Technical effectiveness on application-owned code |
| Raw bundle coverage | **{raw['coveragePercent']:.6f}%** | {raw['coveredBytes']:,} / {raw['knownBytes']:,} generated bytes | Runtime/collector diagnostics |

## Interpretation

- **DANTE-compatible** intentionally reproduces DANTE's historical main.js, source-map-line, character-unit and function-range behavior. It is the comparison metric, not the preferred modern quality metric.
- **Modern application coverage** excludes non-application source-map entries and reports original source-mapped line coverage. This is the primary technical result.
- **Raw bundle coverage** includes every JavaScript bundle accepted by the collector. Framework and vendor size can dominate it, so it is diagnostic rather than a source-code quality result.

## DANTE compatibility qualification

{dante['compatibilityNote']}
"""
    path.write_text(text, encoding="utf-8")


def main() -> int:
    args = parse_args()
    coverage_root = Path(args.coverage_root).resolve()
    output_dir = Path(args.output_dir).resolve() if args.output_dir else coverage_root
    output_dir.mkdir(parents=True, exist_ok=True)

    modern_path = coverage_root / "modern-raw-run" / "suite-coverage.json"
    dante_path = coverage_root / "dante-compatible-run" / "test-raw-ranges.json"

    if not modern_path.exists():
        raise FileNotFoundError(modern_path)
    if not dante_path.exists():
        raise FileNotFoundError(dante_path)

    modern_report = load_json(modern_path)
    dante_raw_tests = load_json(dante_path)

    main_pattern = re.compile(args.main_script_regex, re.IGNORECASE)
    include_source = re.compile(args.app_source_include_regex, re.IGNORECASE)
    exclude_source = re.compile(args.app_source_exclude_regex, re.IGNORECASE)
    source_is_application = application_source_predicate(
        include_source,
        exclude_source,
    )

    main_script = find_script(modern_report, main_pattern)
    source_url = args.source_url or str(main_script.get("url", ""))
    if not source_url:
        raise RuntimeError("Could not determine main script URL")

    raw_source = fetch_text(source_url)
    dante_source = "\n".join(raw_source.splitlines())
    source_map = parse_source_map(fetch_text(args.sourcemap_url))

    raw_bundle = {
        "metric": "raw_all_bundle_v8_detailed_byte_coverage",
        "knownBytes": int(modern_report.get("knownBytes", 0)),
        "coveredBytes": int(modern_report.get("coveredBytes", 0)),
        "coveragePercent": float(modern_report.get("coveragePercent", 0.0)),
        "scripts": modern_report.get("scripts", []),
        "use": "Collector/runtime diagnostic; not application source coverage.",
    }

    modern = build_modern_application_report(
        modern_report,
        raw_source,
        source_map,
        main_pattern,
        source_is_application,
    )

    dante = build_dante_report(
        dante_raw_tests,
        dante_source,
        source_map,
        main_pattern,
        args.dante_src_code_folder,
        args.dante_src_code_files_to_exclude,
        args.dante_empty_exclusion_behavior,
    )

    summary = {
        "rawBundleCoverage": raw_bundle,
        "modernApplicationCoverage": modern,
        "danteCompatibleCoverage": dante,
        "configuration": {
            "sourceUrl": source_url,
            "sourceMapUrl": args.sourcemap_url,
            "mainScriptRegex": args.main_script_regex,
            "applicationSourceIncludeRegex": args.app_source_include_regex,
            "applicationSourceExcludeRegex": args.app_source_exclude_regex,
            "danteSrcCodeFolder": args.dante_src_code_folder,
            "danteSrcCodeFilesToExclude": args.dante_src_code_files_to_exclude,
            "danteEmptyExclusionBehavior": args.dante_empty_exclusion_behavior,
        },
    }

    write_json(output_dir / "raw-bundle-coverage.json", raw_bundle)
    write_json(output_dir / "modern-application-coverage.json", modern)
    write_modern_csv(output_dir / "modern-application-coverage.csv", modern)
    write_json(output_dir / "dante-compatible-coverage.json", dante)
    write_json(output_dir / "coverage-summary.json", summary)
    write_summary_csv(output_dir / "coverage-summary.csv", summary)
    write_summary_markdown(output_dir / "coverage-summary.md", summary)

    print("THREE_COVERAGE_RESULTS")
    print(f"DANTE-compatible : {dante['coveragePercent']:.4f}%")
    print(f"Modern application: {modern['coveragePercent']:.6f}%")
    print(f"Raw bundle       : {raw_bundle['coveragePercent']:.6f}%")
    print(f"Output           : {output_dir}")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except Exception as exc:  # intentional CLI boundary
        print(f"ERROR: {exc}", file=sys.stderr)
        raise
