#!/usr/bin/env python3
from __future__ import annotations

import argparse
import csv
import importlib.util
import json
import sys
from decimal import Decimal, ROUND_HALF_UP
from pathlib import Path
from typing import Any

DANTE_EXCLUDES = [
    "jsmart", "backbone", "js", "bootstrap.clickonmouseover", "i18n",
    "underscore-min", "bootstrap-tour", "button", "collapse", "dropdown",
    "modal", "popover", "tooltip", "analytics",
]
KNOWN_THIRD_PARTY = ("google", "facebook", "twitter")


def load_json(path: Path) -> dict[str, Any]:
    with path.open("r", encoding="utf-8") as handle:
        return json.load(handle)


def dump_json(path: Path, data: Any) -> None:
    path.write_text(
        json.dumps(data, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )


def half_up(value: Decimal, places: int) -> Decimal:
    quantum = Decimal("1").scaleb(-places)
    return value.quantize(quantum, rounding=ROUND_HALF_UP)


def dante_ignore(url: str) -> tuple[bool, str]:
    script_url = (url or "").strip()
    if ".js" not in script_url:
        return True, "not_js"
    if ".min.js" in script_url:
        return True, "minified"
    if any(token in script_url for token in KNOWN_THIRD_PARTY):
        return True, "known_third_party"

    index_last_separator = script_url.rfind("/")
    index_extension = script_url.find(".js")
    if index_extension < 0:
        return True, "not_js"

    basename = script_url[index_last_separator + 1:index_extension]
    for token in DANTE_EXCLUDES:
        if token in basename:
            return True, f"exclude:{token}"
    return False, "accepted"


def merge_count(intervals: list[tuple[int, int]]) -> int:
    normalized: list[tuple[int, int]] = []
    for start, end in intervals:
        start = max(int(start), 1)
        end = int(end)
        if end >= start:
            normalized.append((start, end))

    if not normalized:
        return 0

    normalized.sort()
    total = 0
    current_start, current_end = normalized[0]

    for start, end in normalized[1:]:
        if start <= current_end + 1:
            current_end = max(current_end, end)
        else:
            total += current_end - current_start + 1
            current_start, current_end = start, end

    total += current_end - current_start + 1
    return total


def import_helper(path: Path):
    spec = importlib.util.spec_from_file_location(
        "testception_dante_helper", path
    )
    if spec is None or spec.loader is None:
        raise RuntimeError(f"Cannot import helper: {path}")
    module = importlib.util.module_from_spec(spec)
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)
    return module


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--coverage-root", required=True)
    parser.add_argument(
        "--application-prefix",
        default="http://localhost:3000/scripts/",
    )
    args = parser.parse_args()

    coverage_root = Path(args.coverage_root).resolve()
    raw_path = (
        coverage_root / "dante-compatible-run" / "test-raw-ranges.json"
    )
    modern_path = (
        coverage_root / "modern-raw-run" / "suite-coverage.json"
    )

    if not raw_path.is_file():
        raise RuntimeError(f"DANTE raw ranges missing: {raw_path}")
    if not modern_path.is_file():
        raise RuntimeError(f"Modern/raw suite report missing: {modern_path}")

    helper = import_helper(
        Path(__file__).with_name("coverage_postprocessor.py")
    )
    raw_tests = load_json(raw_path)
    modern = load_json(modern_path)

    script_state: dict[str, dict[str, Any]] = {}
    filter_diagnostics: dict[str, str] = {}

    successful_tests = 0
    raw_ranges_seen = 0
    rejected_ranges = 0
    whole_file_ranges = 0
    accepted_range_occurrences = 0

    def ensure_script(url: str) -> dict[str, Any]:
        state = script_state.get(url)
        if state is not None:
            return state

        source = helper.fetch_text(url)
        allowed, denominator, _ = helper.build_dante_allowed_characters(
            source, []
        )

        state = {
            "source": source,
            "allowed": allowed,
            "denominator": int(denominator),
            "intervals": [],
            "cache": {},
        }
        script_state[url] = state
        return state

    for test in raw_tests.get("tests", []):
        if str(test.get("status", "")) != "PASSED":
            continue
        successful_tests += 1

        for script in test.get("scripts", []):
            url = str(script.get("url", "")).strip()

            # Generated tests may navigate outside Dimeshift, e.g. GitHub.
            # External scripts must not enter Dimeshift coverage.
            if not url.startswith("http://localhost:3000/"):
                filter_diagnostics[url] = "out_of_subject_origin"
                continue

            ignored, reason = dante_ignore(url)
            filter_diagnostics[url] = reason
            if ignored:
                continue

            state = ensure_script(url)
            source = state["source"]
            allowed = state["allowed"]
            cache = state["cache"]

            for raw_range in script.get("ranges", []):
                raw_ranges_seen += 1
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

                key = (start, end, count)
                is_new = key not in cache

                if is_new:
                    if not helper.dante_can_add(allowed, start, end):
                        cache[key] = None
                    else:
                        replacements = helper.dante_overlapping_ranges(
                            source, start, end, count
                        )
                        ranges_to_add = replacements or [
                            (start, end, count)
                        ]
                        cache[key] = tuple(
                            (accepted_start, accepted_end)
                            for accepted_start, accepted_end, _ in ranges_to_add
                        )

                cached = cache[key]
                if cached is None:
                    rejected_ranges += 1
                    continue

                accepted_range_occurrences += len(cached)
                if is_new:
                    state["intervals"].extend(cached)

    if not script_state:
        raise RuntimeError(
            "No Dimeshift scripts survived the original DANTE filter."
        )

    per_script: list[dict[str, Any]] = []
    rounded_fractions: list[Decimal] = []
    aggregate_covered = 0
    aggregate_total = 0

    for url in sorted(script_state):
        state = script_state[url]
        denominator = int(state["denominator"])
        covered = merge_count(state["intervals"])

        if covered > denominator:
            raise RuntimeError(
                f"Invalid DANTE script coverage for {url}: "
                f"covered={covered} > total={denominator}"
            )

        fraction = (
            Decimal(covered) / Decimal(denominator)
            if denominator > 0
            else Decimal("0")
        )
        rounded_fraction = half_up(fraction, 4)
        rounded_fractions.append(rounded_fraction)
        percent = half_up(
            rounded_fraction * Decimal("100"), 4
        )

        aggregate_covered += covered
        aggregate_total += denominator

        per_script.append({
            "url": url,
            "coveredCharacterUnits": covered,
            "totalCharacterUnits": denominator,
            "roundedCoverageFraction": float(rounded_fraction),
            "coveragePercent": float(percent),
        })

    dante_percent = half_up(
        (
            sum(rounded_fractions, Decimal("0"))
            / Decimal(len(rounded_fractions))
        ) * Decimal("100"),
        4,
    )

    dante_report = {
        "metric": "dante_compatible_legacy_multiscript_byte_coverage",
        "coveragePercent": float(dante_percent),
        "aggregation": (
            "arithmetic mean of per-script covered/total fractions; "
            "each fraction rounded to 4 decimals before averaging"
        ),
        "coverageType": "bytes",
        "successfulTestsInUnion": successful_tests,
        "scriptCount": len(per_script),
        "aggregateCoveredCharacterUnits": aggregate_covered,
        "aggregateTotalCharacterUnits": aggregate_total,
        "rawRangesSeen": raw_ranges_seen,
        "acceptedRangeOccurrences": accepted_range_occurrences,
        "rejectedRanges": rejected_ranges,
        "wholeFileRangesExcluded": whole_file_ranges,
        "scriptNamesToExclude": DANTE_EXCLUDES,
        "knownThirdPartyFilter": list(KNOWN_THIRD_PARTY),
        "nestedFunctionSemantics": (
            "corrected DANTE historical heuristic; "
            "negative-start replacements are forbidden"
        ),
        "perScript": per_script,
        "filterDiagnostics": [
            {"url": url, "decision": decision}
            for url, decision in sorted(filter_diagnostics.items())
        ],
    }

    scripts = modern.get("scripts", [])
    app_scripts = [
        script
        for script in scripts
        if str(script.get("url", "")).startswith(
            args.application_prefix
        )
    ]

    modern_total = sum(
        int(script.get("knownBytes", 0))
        for script in app_scripts
    )
    modern_covered = sum(
        int(script.get("coveredBytes", 0))
        for script in app_scripts
    )
    modern_percent = (
        modern_covered * 100.0 / modern_total
        if modern_total else 0.0
    )

    modern_report = {
        "metric": "direct_application_js_byte_coverage",
        "coveragePercent": round(modern_percent, 6),
        "coveredCoverageUnits": modern_covered,
        "totalCoverageUnits": modern_total,
        "coverageUnit": "V8 generated bytes",
        "applicationSelection": (
            f"url starts with {args.application_prefix!r}"
        ),
        "sourceMapUsed": False,
        "qualification": (
            "Dimeshift serves application JavaScript as separate source "
            "files under /scripts/; this is direct application-script byte "
            "coverage rather than source-mapped line coverage."
        ),
        "scriptCount": len(app_scripts),
        "scripts": app_scripts,
    }

    raw_total = int(modern.get("knownBytes", 0))
    raw_covered = int(modern.get("coveredBytes", 0))
    raw_percent = (
        raw_covered * 100.0 / raw_total
        if raw_total else 0.0
    )

    raw_report = {
        "metric": "raw_same_origin_js_byte_coverage",
        "coveragePercent": round(raw_percent, 6),
        "coveredCoverageUnits": raw_covered,
        "totalCoverageUnits": raw_total,
        "coverageUnit": "V8 generated bytes",
        "scriptCount": len(scripts),
        "includeRegex": modern.get("includeRegex", ""),
        "excludeRegex": modern.get("excludeRegex", ""),
    }

    summary = {
        "application": "dimeshift",
        "profileMode": "legacy-multiscript",
        "danteCompatible": dante_report,
        "modernApplication": modern_report,
        "rawBundle": raw_report,
    }

    dump_json(
        coverage_root / "dante-compatible-coverage.json",
        dante_report,
    )
    dump_json(
        coverage_root / "modern-application-coverage.json",
        modern_report,
    )
    dump_json(
        coverage_root / "raw-bundle-coverage.json",
        raw_report,
    )
    dump_json(
        coverage_root / "coverage-summary.json",
        summary,
    )

    with (
        coverage_root / "modern-application-coverage.csv"
    ).open("w", encoding="utf-8", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(
            ["url", "known_bytes", "covered_bytes", "coverage_percent"]
        )
        for script in app_scripts:
            writer.writerow([
                script.get("url", ""),
                script.get("knownBytes", 0),
                script.get("coveredBytes", 0),
                script.get("coveragePercent", 0),
            ])

    with (
        coverage_root / "coverage-summary.csv"
    ).open("w", encoding="utf-8", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(
            ["metric", "coverage_percent", "covered", "total", "unit"]
        )
        writer.writerow([
            "DANTE-compatible",
            f"{float(dante_percent):.4f}",
            aggregate_covered,
            aggregate_total,
            "historical per-script character units; final arithmetic mean",
        ])
        writer.writerow([
            "Modern application",
            f"{modern_percent:.6f}",
            modern_covered,
            modern_total,
            "direct application JS bytes",
        ])
        writer.writerow([
            "Raw bundle",
            f"{raw_percent:.6f}",
            raw_covered,
            raw_total,
            "same-origin JS bytes",
        ])

    markdown = (
        "# Three-Metric Coverage Summary — Dimeshift\n\n"
        "| Metric | Result | Covered / total | Primary use |\n"
        "|---|---:|---:|---|\n"
        f"| DANTE-compatible coverage | **{float(dante_percent):.4f}%** "
        f"| {len(per_script)} scripts; arithmetic mean | "
        "Academic comparison with historical DANTE |\n"
        f"| Modern application coverage | **{modern_percent:.6f}%** "
        f"| {modern_covered:,} / {modern_total:,} direct JS bytes | "
        "Technical effectiveness on application-owned `/scripts/` code |\n"
        f"| Raw bundle coverage | **{raw_percent:.6f}%** "
        f"| {raw_covered:,} / {raw_total:,} JS bytes | "
        "Runtime/collector diagnostics |\n\n"
        "## Dimeshift compatibility profile\n\n"
        "Dimeshift's original DANTE configuration uses no sourcemap. "
        "It uses byte coverage and script-name exclusions. The "
        "DANTE-compatible result applies those historical filters, "
        "unions successful-test coverage per script, and reports the "
        "arithmetic mean across exercised scripts.\n\n"
        "Modern application coverage is direct application-script byte "
        "coverage for `/scripts/` files, not source-mapped line coverage.\n"
    )
    (coverage_root / "coverage-summary.md").write_text(
        markdown, encoding="utf-8"
    )

    print("THREE_COVERAGE_RESULTS")
    print(f"DANTE-compatible : {float(dante_percent):.4f}%")
    print(f"Modern application: {modern_percent:.6f}%")
    print(f"Raw bundle       : {raw_percent:.6f}%")
    print(f"Output           : {coverage_root}")


if __name__ == "__main__":
    main()
