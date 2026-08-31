#!/usr/bin/env bash
set -euo pipefail

# Run the same generated JUnit suite twice:
#   1) DANTE mode: callCount=false, detailed=false
#   2) Modern/raw mode: callCount=true, detailed=true
# Then calculate all three final metrics with coverage_postprocessor.py.

SUITE_DIR="${TESTCEPTION_SUITE_DIR:-$(pwd)}"
TEST_CLASS="${TESTCEPTION_TEST_CLASS:-tests.GeneratedTestSuiteFiredTest}"
APP="${TESTCEPTION_APP:-petclinic}"
TECHNIQUE="${TESTCEPTION_COVERAGE_TECHNIQUE:-selenium4-three-metrics}"
COVERAGE_BASE="${TESTCEPTION_COVERAGE_DIR:-$SUITE_DIR/target/coverage-results}"
COVERAGE_ROOT="$COVERAGE_BASE/$APP/$TECHNIQUE"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
POSTPROCESSOR="${TESTCEPTION_POSTPROCESSOR:-$SCRIPT_DIR/coverage_postprocessor.py}"
PYTHON_BIN="${TESTCEPTION_PYTHON:-python3}"
LOG_DIR="${TESTCEPTION_LOG_DIR:-$SUITE_DIR/coverage-run-logs}"
WAIT_URL="${TESTCEPTION_WAIT_URL:-http://localhost:3000/}"
SOURCEMAP_URL="${TESTCEPTION_SOURCEMAP_URL:-http://localhost:3000/main.js.map}"
MAIN_SCRIPT_REGEX="${TESTCEPTION_MAIN_SCRIPT_REGEX:-(?:^|/)main\.js(?:\?.*)?$}"
APP_SOURCE_INCLUDE_REGEX="${TESTCEPTION_APP_SOURCE_INCLUDE_REGEX:-(?:^|/)(?:src|app)/}"
APP_SOURCE_EXCLUDE_REGEX="${TESTCEPTION_APP_SOURCE_EXCLUDE_REGEX:-(?:^|/)(?:node_modules|vendor|webpack)(?:/|$)}"
DANTE_SRC_CODE_FOLDER="${TESTCEPTION_DANTE_SRC_CODE_FOLDER:-src}"
DANTE_SRC_CODE_FILES_TO_EXCLUDE="${TESTCEPTION_DANTE_SRC_CODE_FILES_TO_EXCLUDE:-}"
DANTE_EMPTY_EXCLUSION_BEHAVIOR="${TESTCEPTION_DANTE_EMPTY_EXCLUSION_BEHAVIOR:-java}"
RESET_COMMAND="${TESTCEPTION_RESET_COMMAND:-}"

mkdir -p "$LOG_DIR"
rm -rf "$COVERAGE_ROOT"
mkdir -p "$COVERAGE_ROOT"

wait_for_app() {
    local attempts="${TESTCEPTION_WAIT_ATTEMPTS:-36}"
    local sleep_seconds="${TESTCEPTION_WAIT_SECONDS:-5}"
    local code="000"

    for ((i=1; i<=attempts; i++)); do
        code="$(curl -sS -o /dev/null -w '%{http_code}' "$WAIT_URL" 2>/dev/null || true)"
        [[ -n "$code" ]] || code="000"
        echo "Application readiness $i/$attempts: HTTP $code"
        if [[ "$code" =~ ^[23] ]]; then
            return 0
        fi
        sleep "$sleep_seconds"
    done

    echo "Application did not become ready at $WAIT_URL" >&2
    return 1
}

reset_application() {
    local phase="$1"
    if [[ -n "$RESET_COMMAND" ]]; then
        echo "Resetting application before $phase coverage..."
        bash -lc "$RESET_COMMAND"
    else
        echo "WARNING: TESTCEPTION_RESET_COMMAND is empty."
        echo "The $phase run will use the current application/database state."
    fi
    wait_for_app
}

run_phase() {
    local phase="$1"
    local mode="$2"
    local include_regex="$3"
    local log_file="$LOG_DIR/${phase}.log"

    reset_application "$phase"

    echo "============================================================"
    echo "Running $phase coverage"
    echo "mode=$mode"
    echo "include=$include_regex"
    echo "============================================================"

    (
        cd "$SUITE_DIR"
        export TESTCEPTION_APP="$APP"
        export TESTCEPTION_COVERAGE_DIR="$COVERAGE_BASE"
        export TESTCEPTION_COVERAGE_TECHNIQUE="$TECHNIQUE"
        export TESTCEPTION_COVERAGE_MODE="$mode"
        export TESTCEPTION_COVERAGE_INCLUDE_REGEX="$include_regex"
        export TESTCEPTION_COVERAGE_EXCLUDE_REGEX="${TESTCEPTION_COVERAGE_EXCLUDE_REGEX:-(?!)}"

        set -o pipefail
        mvn -Dtest="$TEST_CLASS" test 2>&1 | tee "$log_file"
    )
}

# DANTE needs its own run because V8 precise coverage cannot be detailed=true
# and detailed=false simultaneously.
run_phase \
    "dante-compatible" \
    "dante" \
    "${TESTCEPTION_DANTE_INCLUDE_REGEX:-^http://localhost:3000/main\\.js(?:\\?.*)?$}"

run_phase \
    "modern-raw" \
    "modern_raw" \
    "${TESTCEPTION_RAW_INCLUDE_REGEX:-^http://localhost:3000/.*\\.js(?:\\?.*)?$}"

# TESTCEPTION_POSTPROCESS_POLICY_V1
if [[ "${TESTCEPTION_RUN_POSTPROCESS:-true}" == "true" ]]; then
    echo
    echo "Running coverage post-processing..."
    "$PYTHON_BIN" "$POSTPROCESSOR" \
        --coverage-root "$COVERAGE_ROOT" \
        --sourcemap-url "$SOURCEMAP_URL" \
        --main-script-regex "$MAIN_SCRIPT_REGEX" \
        --app-source-include-regex "$APP_SOURCE_INCLUDE_REGEX" \
        --app-source-exclude-regex "$APP_SOURCE_EXCLUDE_REGEX" \
        --dante-src-code-folder "$DANTE_SRC_CODE_FOLDER" \
        --dante-src-code-files-to-exclude "$DANTE_SRC_CODE_FILES_TO_EXCLUDE" \
        --dante-empty-exclusion-behavior "$DANTE_EMPTY_EXCLUSION_BEHAVIOR" \
        --output-dir "$COVERAGE_ROOT"
else
    echo
    echo "POSTPROCESS_SKIPPED"
    echo "Application : $APP"
    echo "Reason      : TESTCEPTION_RUN_POSTPROCESS=false"
    echo "DANTE raw   : $COVERAGE_ROOT/dante-compatible-run/test-raw-ranges.json"
    echo "Modern/raw  : $COVERAGE_ROOT/modern-raw-run/suite-coverage.json"
fi

echo
echo "Final reports:"
find "$COVERAGE_ROOT" -maxdepth 1 -type f -printf '%f\n' | sort
