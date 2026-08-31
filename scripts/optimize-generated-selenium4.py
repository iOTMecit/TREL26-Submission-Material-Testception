#!/usr/bin/env python3
from pathlib import Path
import re
import sys

MARKER = "TESTCEPTION_ADAPTIVE_SLEEP_V1"

if len(sys.argv) != 2:
    raise SystemExit(
        "Usage: optimize-generated-selenium4.py "
        "<GeneratedTestSuiteFiredTest.java>"
    )

path = Path(sys.argv[1]).resolve()
if not path.is_file():
    raise SystemExit(f"Generated Selenium4 test file not found: {path}")

text = path.read_text(encoding="utf-8")

if MARKER in text:
    print("Adaptive Selenium4 runtime already installed: " + str(path))
    raise SystemExit(0)

class_match = re.search(
    r"(public\s+class\s+GeneratedTestSuiteFiredTest\s*\{\s*)",
    text,
)
if not class_match:
    raise SystemExit(
        "GeneratedTestSuiteFiredTest class declaration was not found."
    )

# Replace literal generated sleeps BEFORE helper injection.
text, sleep_count = re.subn(
    r"Thread\.sleep\(\s*(\d+)\s*\);",
    r"adaptiveSleep(\1);",
    text,
)

runtime_helper = r'''
	// TESTCEPTION_ADAPTIVE_SLEEP_V1
	private static final double TESTCEPTION_WAIT_SCALE =
		resolveTestceptionWaitScale();

	private static final boolean TESTCEPTION_VISUAL_HIGHLIGHT =
		Boolean.parseBoolean(
			System.getenv().getOrDefault(
				"TESTCEPTION_VISUAL_HIGHLIGHT",
				"true"
			)
		);

	private static double resolveTestceptionWaitScale() {
		String raw = System.getenv().getOrDefault(
			"TESTCEPTION_WAIT_SCALE",
			"1.0"
		);

		try {
			double value = Double.parseDouble(raw.trim());
			if (value < 0.0) return 0.0;
			if (value > 10.0) return 10.0;
			return value;
		} catch (Exception ignored) {
			return 1.0;
		}
	}

	private static void adaptiveSleep(long baseMilliseconds)
			throws InterruptedException {
		long scaled = Math.round(
			baseMilliseconds * TESTCEPTION_WAIT_SCALE
		);

		if (scaled > 0L) {
			Thread.sleep(scaled);
		}
	}

'''

insert_at = class_match.end(1)
text = text[:insert_at] + runtime_helper + text[insert_at:]

highlight_pattern = (
    r"(private\s+void\s+highlight\s*"
    r"\(\s*WebElement\s+element\s*\)\s*\{\s*)"
)
text, highlight_count = re.subn(
    highlight_pattern,
    r"\1\n\t\tif (!TESTCEPTION_VISUAL_HIGHLIGHT) return;\n",
    text,
    count=1,
)

path.write_text(text, encoding="utf-8")

print("Adaptive runtime installed : " + str(path))
print("Literal sleeps converted   : " + str(sleep_count))
print("Highlight guard installed  : " + str(highlight_count))
print("Runtime control             : TESTCEPTION_WAIT_SCALE (default 1.0)")
print("Visual highlighting         : TESTCEPTION_VISUAL_HIGHLIGHT (default true)")
