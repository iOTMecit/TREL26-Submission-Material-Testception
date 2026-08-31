#!/usr/bin/env python3
"""
Inject Testception's Dimeshift per-test backend reset hook into a generated
Selenium4 GeneratedTestSuiteFiredTest.java.

Design goals:
- Dimeshift only; other applications are unchanged.
- Run the DB reset at the very beginning of TestWatcher.starting().
- Preserve the existing fresh-browser recycle/rebind and coverage lifecycle.
- Fail fast if the backend reset fails; do not silently measure contaminated state.
- Be idempotent.
"""

from __future__ import annotations

import re
import stat
import sys
from pathlib import Path

MARKER = "TESTCEPTION_PER_TEST_BACKEND_RESET_V1"

RESET_SCRIPT = r"""#!/usr/bin/env bash
set -Eeuo pipefail

SUITE_DIR="$(
    cd "$(dirname "${BASH_SOURCE[0]}")/.." &&
    pwd
)"

APP_DIR="$(
    cd "$SUITE_DIR/.." &&
    pwd
)"

ROOT_DIR="$(
    cd "$APP_DIR/../../.." &&
    pwd
)"

SQL_FILE="$APP_DIR/testsuite-dimeshift/src/main/resources/dimeshift.sql"
PROPERTIES_FILE="$ROOT_DIR/dante/src/main/resources/app.properties"

[[ -f "$SQL_FILE" ]] || {
    echo "DIMESHIFT_DB_RESET_ERROR: SQL dump not found: $SQL_FILE" >&2
    exit 2
}

DB_PORT="${TESTCEPTION_DB_PORT:-}"

if [[ -z "$DB_PORT" && -f "$PROPERTIES_FILE" ]]; then
    DB_PORT="$(
        grep -E '^db_port=' "$PROPERTIES_FILE" \
        | tail -n 1 \
        | cut -d= -f2- \
        | tr -d '[:space:]' \
        || true
    )"
fi

DB_PORT="${DB_PORT:-3306}"
DB_NAME="${TESTCEPTION_DB_NAME:-walletjs}"
DB_USER="${TESTCEPTION_DB_USER:-root}"
DB_PASSWORD="${TESTCEPTION_DB_PASSWORD:-root}"

run_with_host_mysql() {
    command -v mysql >/dev/null 2>&1 || return 1

    MYSQL_PWD="$DB_PASSWORD" \
        mysql \
        --protocol=TCP \
        -h 127.0.0.1 \
        -P "$DB_PORT" \
        -u "$DB_USER" \
        "$DB_NAME" \
        < "$SQL_FILE"
}

find_mysql_container() {
    local id name ports

    while IFS='|' read -r id name ports; do
        [[ -n "$id" ]] || continue

        if [[ "$ports" == *"${DB_PORT}->3306/tcp"* ]] || \
           [[ "$name" == *dimeshift* ]]; then
            if docker exec "$id" sh -lc \
                'command -v mysql >/dev/null 2>&1' \
                >/dev/null 2>&1; then
                echo "$id"
                return 0
            fi
        fi
    done < <(
        docker ps --format '{{.ID}}|{{.Names}}|{{.Ports}}'
    )

    return 1
}

run_with_container_mysql() {
    command -v docker >/dev/null 2>&1 || return 1

    local container
    container="$(find_mysql_container)" || return 1

    docker exec -i \
        -e MYSQL_PWD="$DB_PASSWORD" \
        "$container" \
        mysql \
        -u "$DB_USER" \
        "$DB_NAME" \
        < "$SQL_FILE"
}

echo "DIMESHIFT_DB_RESET_START | db=$DB_NAME | port=$DB_PORT"

for attempt in 1 2 3 4 5; do
    if run_with_host_mysql; then
        echo "DIMESHIFT_DB_RESET_DONE | transport=host-mysql | attempt=$attempt"
        exit 0
    fi

    if run_with_container_mysql; then
        echo "DIMESHIFT_DB_RESET_DONE | transport=docker-mysql | attempt=$attempt"
        exit 0
    fi

    echo "DIMESHIFT_DB_RESET_RETRY | attempt=$attempt" >&2
    sleep 1
done

echo "DIMESHIFT_DB_RESET_ERROR: could not restore walletjs from $SQL_FILE" >&2
exit 3
"""

JAVA_HELPER = r"""
	// TESTCEPTION_PER_TEST_BACKEND_RESET_V1
	private void runPerTestReset(String testName) {
		String app = System.getenv("TESTCEPTION_APP");

		if (app == null || !"dimeshift".equalsIgnoreCase(app.trim())) {
			return;
		}

		String enabled = System.getenv("TESTCEPTION_PER_TEST_RESET");
		if (enabled != null && "false".equalsIgnoreCase(enabled.trim())) {
			System.out.println(
				"DIMESHIFT_DB_RESET_SKIPPED | test="
				+ testName
				+ " | reason=TESTCEPTION_PER_TEST_RESET=false"
			);
			return;
		}

		String suiteDir = System.getenv("TESTCEPTION_SUITE_DIR");
		if (suiteDir == null || suiteDir.trim().isEmpty()) {
			throw new IllegalStateException(
				"TESTCEPTION_SUITE_DIR is required for Dimeshift per-test reset"
			);
		}

		java.nio.file.Path resetScript =
			java.nio.file.Paths.get(
				suiteDir,
				"scripts",
				"reset-dimeshift-db.sh"
			);

		if (!java.nio.file.Files.isRegularFile(resetScript)) {
			throw new IllegalStateException(
				"Dimeshift per-test reset script not found: "
				+ resetScript
			);
		}

		System.out.println(
			"DIMESHIFT_PER_TEST_RESET_START | test=" + testName
		);

		try {
			Process process =
				new ProcessBuilder(
					"bash",
					resetScript.toAbsolutePath().toString()
				)
				.inheritIO()
				.start();

			int exitCode = process.waitFor();

			if (exitCode != 0) {
				throw new IllegalStateException(
					"Dimeshift backend reset failed for "
					+ testName
					+ " with exit code "
					+ exitCode
				);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(
				"Dimeshift backend reset interrupted for "
					+ testName,
				e
			);
		} catch (java.io.IOException e) {
			throw new RuntimeException(
				"Could not execute Dimeshift backend reset for "
					+ testName,
				e
			);
		}

		System.out.println(
			"DIMESHIFT_PER_TEST_RESET_DONE | test=" + testName
		);
	}

"""


def find_project(java_file: Path) -> Path:
    for parent in java_file.parents:
        if (
            parent.name.startswith("testsuite-")
            and "-selenium4" in parent.name
        ):
            return parent

    raise RuntimeError(
        "Could not infer Selenium4 project root from: "
        + str(java_file)
    )


def is_dimeshift_project(project: Path) -> bool:
    return (
        project.parent.name.lower() == "dimeshift"
        or "dimeshift" in project.name.lower()
    )


def write_reset_script(project: Path) -> Path:
    target = project / "scripts" / "reset-dimeshift-db.sh"
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(RESET_SCRIPT, encoding="utf-8")

    mode = target.stat().st_mode
    target.chmod(
        mode
        | stat.S_IXUSR
        | stat.S_IXGRP
        | stat.S_IXOTH
    )
    return target


def inject(java_file: Path) -> bool:
    project = find_project(java_file)

    if not is_dimeshift_project(project):
        print(
            "Per-test backend reset: skipped non-Dimeshift project: "
            + str(project)
        )
        return False

    text = java_file.read_text(encoding="utf-8")
    reset_script = write_reset_script(project)

    if MARKER in text:
        print(
            "Per-test backend reset already present: "
            + str(java_file)
        )
        print("Reset script: " + str(reset_script))
        return False

    starting = re.search(
        r"(?m)^(?P<indent>[ \t]*)protected void starting"
        r"\(Description description\) \{\s*$",
        text,
    )

    if not starting:
        raise RuntimeError(
            "Could not find "
            "TestWatcher.starting(Description description) in "
            + str(java_file)
        )

    body_indent = starting.group("indent") + "\t"
    insert_at = starting.end()

    text = (
        text[:insert_at]
        + "\n"
        + body_indent
        + "runPerTestReset(name(description));"
        + text[insert_at:]
    )

    rule = re.search(r"(?m)^[ \t]*@Rule\s*$", text)
    if not rule:
        raise RuntimeError(
            "Could not find @Rule anchor in "
            + str(java_file)
        )

    text = (
        text[:rule.start()]
        + JAVA_HELPER
        + text[rule.start():]
    )

    java_file.write_text(text, encoding="utf-8")

    print(
        "Per-test backend reset injected: "
        + str(java_file)
    )
    print("Reset script: " + str(reset_script))
    return True


def main() -> int:
    if len(sys.argv) != 2:
        print(
            "Usage: inject-per-test-reset.py "
            "<GeneratedTestSuiteFiredTest.java>"
        )
        return 2

    java_file = Path(sys.argv[1]).resolve()

    if not java_file.is_file():
        print(
            "Generated Selenium4 test file not found: "
            + str(java_file)
        )
        return 2

    inject(java_file)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
