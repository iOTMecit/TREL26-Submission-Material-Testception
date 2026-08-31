#!/usr/bin/env python3
from __future__ import annotations

import json
import sys
from pathlib import Path


def normalize(intervals):
    out = []
    for start, end in sorted(intervals):
        if end <= start:
            continue
        if out and start <= out[-1][1]:
            out[-1] = (out[-1][0], max(out[-1][1], end))
        else:
            out.append((start, end))
    return out


def total(intervals):
    return sum(end - start for start, end in normalize(intervals))


def subtract(a, b):
    a = normalize(a)
    b = normalize(b)
    out = []
    for start, end in a:
        cursor = start
        for bs, be in b:
            if be <= cursor:
                continue
            if bs >= end:
                break
            if bs > cursor:
                out.append((cursor, min(bs, end)))
            cursor = max(cursor, be)
            if cursor >= end:
                break
        if cursor < end:
            out.append((cursor, end))
    return normalize(out)


def main():
    if len(sys.argv) != 3:
        raise SystemExit(
            "Usage: compare-devtools-raw.py "
            "<devtools-export.json> <raw-bundle-coverage.json>"
        )

    manual = json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
    auto = json.loads(Path(sys.argv[2]).read_text(encoding="utf-8"))

    manual_by_url = {
        item["url"]: item
        for item in manual
        if isinstance(item, dict)
        and item.get("url", "").startswith("http://localhost:3000/")
        and item.get("url", "").split("?", 1)[0].endswith(".js")
    }

    root = auto.get("rawBundleCoverage", auto)
    auto_by_url = {
        item["url"]: item
        for item in root.get("scripts", [])
    }

    urls = sorted(set(manual_by_url) | set(auto_by_url))
    print()
    print(
        f"{'bundle':<18}{'DevTools %':>12}{'Testception %':>16}"
        f"{'delta pp':>11}{'auto-extra':>13}{'auto-missing':>15}"
    )
    print("-" * 85)

    manual_known_total = manual_covered_total = 0
    auto_known_total = auto_covered_total = 0

    for url in urls:
        m = manual_by_url.get(url, {})
        a = auto_by_url.get(url, {})
        name = url.rsplit("/", 1)[-1]

        manual_ranges = [
            (int(r["start"]), int(r["end"]))
            for r in m.get("ranges", [])
        ]
        auto_ranges = [
            (int(r["startOffset"]), int(r["endOffset"]))
            for r in a.get("coveredRanges", [])
        ]

        manual_known = len(m.get("text", ""))
        auto_known = int(a.get("knownBytes", 0))
        manual_covered = total(manual_ranges)
        auto_covered = total(auto_ranges)

        manual_pct = 100.0 * manual_covered / manual_known if manual_known else 0.0
        auto_pct = 100.0 * auto_covered / auto_known if auto_known else 0.0
        extra = total(subtract(auto_ranges, manual_ranges))
        missing = total(subtract(manual_ranges, auto_ranges))

        print(
            f"{name:<18}{manual_pct:>11.4f}%{auto_pct:>15.4f}%"
            f"{auto_pct-manual_pct:>+10.4f}{extra:>13,d}{missing:>15,d}"
        )

        manual_known_total += manual_known
        manual_covered_total += manual_covered
        auto_known_total += auto_known
        auto_covered_total += auto_covered

    manual_pct = (
        100.0 * manual_covered_total / manual_known_total
        if manual_known_total else 0.0
    )
    auto_pct = (
        100.0 * auto_covered_total / auto_known_total
        if auto_known_total else 0.0
    )

    print("-" * 85)
    print(
        f"{'TOTAL':<18}{manual_pct:>11.4f}%{auto_pct:>15.4f}%"
        f"{auto_pct-manual_pct:>+10.4f}"
    )
    print()
    print(f"DevTools known/covered    : {manual_known_total:,} / {manual_covered_total:,}")
    print(f"Testception known/covered : {auto_known_total:,} / {auto_covered_total:,}")
    print()


if __name__ == "__main__":
    main()
