#!/usr/bin/env python3
import re
import sys
import time

if len(sys.argv) != 2:
    raise SystemExit(
        "Usage: testception-progress.py <tests-per-coverage-pass>"
    )

try:
    tests_per_pass = max(1, int(sys.argv[1]))
except Exception as exc:
    raise SystemExit(f"Invalid test count: {sys.argv[1]}") from exc

# Testception currently executes the generated Selenium suite twice:
#   1) DANTE
#   2) MODERN_RAW
# Modern Application coverage is derived from the MODERN_RAW capture.
expected_passes = 2
overall_total = tests_per_pass * expected_passes

started = time.monotonic()
overall_done = 0
current_mode = "STARTING"
mode_done = 0
last_bar_len = 0
summary_lines_remaining = 0
step_skipped_count = 0

MODE_RE = re.compile(r"\bmode=([A-Z_]+)")
END_RE = re.compile(r"SELENIUM4_COVERAGE_TEST_END:")

# Only surface failures that can affect the test/coverage run itself.
# STEP_SKIPPED is intentionally handled before this list and remains
# available in the full tee log.
FATAL_HINTS = (
    "[ERROR]",
    "BUILD FAILURE",
    "<<< FAILURE",
    "SELENIUM4_DRIVER_MARKED_BROKEN",
    "SELENIUM4_COVERAGE_ERROR",
    "ERROR:",
)

def fmt_duration(seconds):
    seconds = max(0, int(round(seconds)))
    hours, rem = divmod(seconds, 3600)
    minutes, secs = divmod(rem, 60)
    return f"{hours:02d}:{minutes:02d}:{secs:02d}"

def clear_bar():
    global last_bar_len
    if last_bar_len:
        sys.stdout.write("\r" + (" " * last_bar_len) + "\r")
        sys.stdout.flush()
        last_bar_len = 0

def render():
    global last_bar_len

    elapsed = time.monotonic() - started
    ratio = min(1.0, overall_done / overall_total)

    width = 32
    filled = int(round(width * ratio))
    bar = "#" * filled + "-" * (width - filled)
    percent = ratio * 100.0

    # ETA is deliberately omitted. Generated scenarios are prefix-based, so
    # later tests are usually longer than early tests; a running-average ETA
    # therefore tends to increase and is misleading.
    line = (
        f"[{bar}] {percent:6.2f}%  "
        f"{current_mode}: {mode_done}/{tests_per_pass}  "
        f"overall: {overall_done}/{overall_total}  "
        f"elapsed {fmt_duration(elapsed)}"
    )

    padding = max(0, last_bar_len - len(line))
    sys.stdout.write("\r" + line + (" " * padding))
    sys.stdout.flush()
    last_bar_len = len(line)

for raw in sys.stdin:
    line = raw.rstrip("\n")

    mode_match = MODE_RE.search(line)
    if "SELENIUM4_COVERAGE_STARTED" in line and mode_match:
        new_mode = mode_match.group(1)
        if new_mode != current_mode:
            current_mode = new_mode
            mode_done = 0
            render()
        continue

    if END_RE.search(line):
        overall_done += 1
        mode_done += 1
        render()
        continue

    # Expected recoverable step failures can be very noisy with hundreds of
    # generated tests. Hide them from progress mode, but count them. Because
    # tee is before this filter, every full STEP_SKIPPED line is still in the
    # normal Testception log file.
    if "STEP_SKIPPED:" in line:
        step_skipped_count += 1
        continue

    if "SELENIUM4_COVERAGE_SUITE:" in line:
        clear_bar()
        print(line, flush=True)
        render()
        continue

    if line.strip() == "THREE_COVERAGE_RESULTS":
        clear_bar()
        print(line, flush=True)
        summary_lines_remaining = 6
        continue

    if summary_lines_remaining > 0:
        print(line, flush=True)
        summary_lines_remaining -= 1
        continue

    if any(token in line for token in FATAL_HINTS):
        clear_bar()
        print(line, flush=True)
        render()
        continue

clear_bar()

elapsed = time.monotonic() - started
print(
    "TESTCEPTION_PROGRESS_COMPLETE | "
    f"observed_test_executions={overall_done}/{overall_total} | "
    f"step_skipped={step_skipped_count} | "
    f"elapsed={fmt_duration(elapsed)}",
    flush=True,
)
