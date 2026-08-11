#!/usr/bin/env bash

set -Eeuo pipefail

# ============================================================
# Testception - One Command Runner
#
# Full experiment:
#   ./run-testception.sh petclinic --full
#
# Coverage only (uses the Selenium4 suite already in the repo):
#   ./run-testception.sh petclinic --coverage
#
# Optional display mode:
#   --headless
#   --headed
#
# Optional full-mode crawl duration override:
#   TESTCEPTION_CRAWL_MINUTES=10 ./run-testception.sh petclinic --full
# ============================================================

ROOT_DIR="$(
    cd "$(dirname "${BASH_SOURCE[0]}")" &&
    pwd
)"

APP="${1:-}"

if [[ -z "$APP" ]]; then
    echo "Usage:"
    echo "  ./run-testception.sh petclinic --full"
    echo "  ./run-testception.sh petclinic --coverage"
    exit 1
fi

shift

MODE=""
HEADLESS="false"

step() {
    echo
    echo "============================================================"
    echo " $1"
    echo "============================================================"
}

fail() {
    echo
    echo "ERROR: $1"
    exit 1
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --full)
            [[ -z "$MODE" ]] || fail "Choose only one execution mode: --full or --coverage"
            MODE="full"
            shift
            ;;

        --coverage)
            [[ -z "$MODE" ]] || fail "Choose only one execution mode: --full or --coverage"
            MODE="coverage"
            shift
            ;;

        --headless)
            HEADLESS="true"
            shift
            ;;

        --headed)
            HEADLESS="false"
            shift
            ;;

        *)
            fail "Unknown option: $1

Usage:
  ./run-testception.sh petclinic --full [--headless|--headed]
  ./run-testception.sh petclinic --coverage [--headless|--headed]"
            ;;
    esac
done

[[ -n "$MODE" ]] || fail "Execution mode is required.

Use one of:
  ./run-testception.sh petclinic --full
  ./run-testception.sh petclinic --coverage"

case "$APP" in
    petclinic)
        ;;
    *)
        fail "Currently supported application: petclinic"
        ;;
esac

# ------------------------------------------------------------
# Paths
# ------------------------------------------------------------

DANTE_DIR="$ROOT_DIR/dante"
RLM_DIR="$ROOT_DIR/Testception/rlm_project"
VENV="$RLM_DIR/.venv-selenium4"
REQUIREMENTS="$ROOT_DIR/requirements.txt"

SUITE="$DANTE_DIR/applications/$APP/testsuite-$APP-selenium4"
SELENIUM4_TEST_FILE="$SUITE/src/test/java/tests/GeneratedTestSuiteFiredTest.java"
LEGACY_GENERATED_TEST="$DANTE_DIR/applications/$APP/testsuite-$APP/src/main/java/tests/GeneratedTestSuiteFired.java"

CRAWL_PARENT="$DANTE_DIR/applications/$APP/localhost"
CRAWL_DIR="$CRAWL_PARENT/crawl-with-inputs"
CRAWL_MINUTES="${TESTCEPTION_CRAWL_MINUTES:-10}"

BRIDGE_SCRIPT="$ROOT_DIR/scripts/prepare-selenium4-suite.py"
MAVEN_REPO="$ROOT_DIR/.m2-selenium4/repository"

COVERAGE_DIR="$ROOT_DIR/coverage-results"
LOG_DIR="$ROOT_DIR/logs"
LOG_FILE="$LOG_DIR/${APP}-${MODE}-three-coverage.log"

# ------------------------------------------------------------
# System requirements
# ------------------------------------------------------------

step "Checking system requirements"

command -v docker >/dev/null 2>&1 || fail "Docker is not installed."
command -v mvn >/dev/null 2>&1 || fail "Maven is not installed."
command -v python3 >/dev/null 2>&1 || fail "Python 3 is not installed."
command -v curl >/dev/null 2>&1 || fail "curl is not installed."

docker info >/dev/null 2>&1 || fail "Docker daemon is not running."

[[ -d "$DANTE_DIR" ]] || fail "DANTE directory not found: $DANTE_DIR"
[[ -d "$SUITE" ]] || fail "Selenium4 test suite not found: $SUITE"
[[ -x "$SUITE/scripts/run-three-coverage.sh" ]] || \
    fail "run-three-coverage.sh not found or not executable: $SUITE/scripts/run-three-coverage.sh"

if [[ "$MODE" == "full" ]]; then
    [[ -d "$RLM_DIR" ]] || fail "RLM directory not found: $RLM_DIR"
    [[ -f "$RLM_DIR/mentor_orchestrator.py" ]] || \
        fail "mentor_orchestrator.py not found: $RLM_DIR/mentor_orchestrator.py"
    [[ -f "$DANTE_DIR/run-crawling.sh" ]] || \
        fail "run-crawling.sh not found: $DANTE_DIR/run-crawling.sh"
    [[ -f "$BRIDGE_SCRIPT" ]] || \
        fail "Selenium4 bridge script not found: $BRIDGE_SCRIPT"
fi

echo "Repository : $ROOT_DIR"
echo "Application: $APP"
echo "Mode       : $MODE"
echo "Headless   : $HEADLESS"

# ------------------------------------------------------------
# FULL mode: LLM + Python environment
# ------------------------------------------------------------

if [[ "$MODE" == "full" ]]; then
    step "Checking LLM configuration"

    [[ -n "${TESTCEPTION_LLM_MODEL:-}" ]] || fail "TESTCEPTION_LLM_MODEL is not set.

Before running --full:
  export TESTCEPTION_LLM_MODEL=\"openrouter/openai/gpt-4o-mini\""

    [[ -n "${OPENROUTER_API_KEY:-}" ]] || fail "OPENROUTER_API_KEY is not set.

Before running --full:
  read -rsp \"OpenRouter API Key: \" OPENROUTER_API_KEY
  echo
  export OPENROUTER_API_KEY"

    echo "LLM model: $TESTCEPTION_LLM_MODEL"
    echo "OpenRouter API key: configured"

    step "Preparing Python RLM environment"

    [[ -f "$REQUIREMENTS" ]] || fail "requirements.txt not found: $REQUIREMENTS"

    if [[ ! -d "$VENV" ]]; then
        echo "Creating virtual environment:"
        echo "  $VENV"
        python3 -m venv "$VENV"
    fi

    # shellcheck disable=SC1091
    source "$VENV/bin/activate"

    echo "Installing Python dependencies from:"
    echo "  $REQUIREMENTS"

    python -m pip install \
        --disable-pip-version-check \
        -r "$REQUIREMENTS"

    # --------------------------------------------------------
    # Java 8 + legacy build
    # --------------------------------------------------------

    step "Preparing Java 8 for Crawljax and DANTE"

    JAVA8_HOME="${JAVA8_HOME:-/usr/lib/jvm/java-8-openjdk-amd64}"

    [[ -x "$JAVA8_HOME/bin/java" ]] || fail "Java 8 not found at $JAVA8_HOME"
    [[ -x "$JAVA8_HOME/bin/javac" ]] || fail "Java 8 javac not found at $JAVA8_HOME"

    export JAVA_HOME="$JAVA8_HOME"
    export PATH="$JAVA_HOME/bin:$PATH"

    java -version
    echo "Using JAVA_HOME=$JAVA_HOME"

    export TESTCEPTION_MAVEN_REPO="$MAVEN_REPO"
    mkdir -p "$MAVEN_REPO"

    step "Building legacy Crawljax"

    cd "$ROOT_DIR/crawljax"
    mvn -q \
        -Dmaven.repo.local="$MAVEN_REPO" \
        -DskipTests \
        install

    step "Building legacy DANTE"

    cd "$DANTE_DIR"
    mvn -q \
        -Dmaven.repo.local="$MAVEN_REPO" \
        -DskipTests \
        install

    # --------------------------------------------------------
    # Fresh crawl
    #
    # The existing dante/run-crawling.sh is invoked as-is.
    # This runner never rewrites or replaces the legacy crawler.
    # --------------------------------------------------------

    step "Cleaning previous Crawljax output"

    rm -rf \
        "$CRAWL_PARENT/crawl0" \
        "$CRAWL_DIR"

    mkdir -p "$LOG_DIR"
    export TESTCEPTION_LOG_DIR="$LOG_DIR"

    step "Running fresh Crawljax exploration"

    echo "Crawl runtime: ${CRAWL_MINUTES} minute(s)"
    echo "Legacy crawler: $DANTE_DIR/run-crawling.sh (unchanged)"

    cd "$DANTE_DIR"
    ./run-crawling.sh \
        "$APP" \
        "$HEADLESS" \
        "$CRAWL_MINUTES"

    [[ -f "$CRAWL_DIR/result.json" ]] || \
        fail "Fresh Crawljax result.json was not generated: $CRAWL_DIR/result.json"

    [[ -d "$CRAWL_DIR/doms" ]] || \
        fail "Fresh Crawljax DOM directory was not generated: $CRAWL_DIR/doms"

    echo "Fresh crawl:"
    echo "  $CRAWL_DIR/result.json"

    # --------------------------------------------------------
    # Recursive LLM
    # --------------------------------------------------------

    step "Running Recursive LLM test generation"

    cd "$RLM_DIR"
    python mentor_orchestrator.py "$APP"

    [[ -f "$LEGACY_GENERATED_TEST" ]] || \
        fail "RLM-generated DANTE suite not found: $LEGACY_GENERATED_TEST"

    LEGACY_TEST_COUNT="$(
        grep -Ec 'public void test[0-9]{3}\(' "$LEGACY_GENERATED_TEST" || true
    )"

    [[ "$LEGACY_TEST_COUNT" -gt 0 ]] || \
        fail "RLM generated zero Java tests."

    echo "RLM-generated tests: $LEGACY_TEST_COUNT"

    # --------------------------------------------------------
    # Legacy generated suite -> Selenium4 coverage-aware suite
    # --------------------------------------------------------

    step "Preparing Selenium4 suite from RLM output"

    python "$BRIDGE_SCRIPT" \
        "$LEGACY_GENERATED_TEST" \
        "$SUITE"

    [[ -f "$SELENIUM4_TEST_FILE" ]] || \
        fail "Selenium4 generated test file not found: $SELENIUM4_TEST_FILE"

    SELENIUM4_TEST_COUNT="$(
        grep -Ec 'public void test[0-9]{3}\(' "$SELENIUM4_TEST_FILE" || true
    )"

    [[ "$SELENIUM4_TEST_COUNT" -gt 0 ]] || \
        fail "Selenium4 bridge produced zero tests."

    [[ "$SELENIUM4_TEST_COUNT" -eq "$LEGACY_TEST_COUNT" ]] || \
        fail "Generated test count mismatch: legacy=$LEGACY_TEST_COUNT selenium4=$SELENIUM4_TEST_COUNT"

    echo "Selenium4 generated tests: $SELENIUM4_TEST_COUNT"

else
    # --------------------------------------------------------
    # COVERAGE mode: do not touch generated suite
    # --------------------------------------------------------

    step "Coverage-only mode"

    [[ -f "$SELENIUM4_TEST_FILE" ]] || \
        fail "Existing Selenium4 generated suite not found: $SELENIUM4_TEST_FILE"

    SELENIUM4_TEST_COUNT="$(
        grep -Ec 'public void test[0-9]{3}\(' "$SELENIUM4_TEST_FILE" || true
    )"

    [[ "$SELENIUM4_TEST_COUNT" -gt 0 ]] || \
        fail "Existing Selenium4 suite contains zero tests."

    echo "Crawljax : skipped"
    echo "RLM      : skipped"
    echo "Generation: skipped"
    echo "Existing Selenium4 tests: $SELENIUM4_TEST_COUNT"
fi

# ------------------------------------------------------------
# Java 17 for Selenium4 execution and coverage
# ------------------------------------------------------------

step "Preparing Java 17"

JAVA17_HOME="${JAVA17_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}"

[[ -x "$JAVA17_HOME/bin/java" ]] || fail "Java 17 not found at $JAVA17_HOME"
[[ -x "$JAVA17_HOME/bin/javac" ]] || fail "Java 17 javac not found at $JAVA17_HOME"

export JAVA_HOME="$JAVA17_HOME"
export PATH="$JAVA_HOME/bin:$PATH"

java -version
echo "Using JAVA_HOME=$JAVA_HOME"

# ------------------------------------------------------------
# Coverage configuration
# ------------------------------------------------------------

step "Configuring coverage"

export TESTCEPTION_SUITE_DIR="$SUITE"
export TESTCEPTION_APP="$APP"
export TESTCEPTION_TEST_CLASS="tests.GeneratedTestSuiteFiredTest"
export TESTCEPTION_COVERAGE_DIR="$COVERAGE_DIR"
export TESTCEPTION_COVERAGE_TECHNIQUE="selenium4-three-metrics"
export TESTCEPTION_HEADLESS="$HEADLESS"

export TESTCEPTION_RESET_COMMAND="$SUITE/scripts/reset-petclinic.sh"
export TESTCEPTION_WAIT_URL="http://localhost:3000/"
export TESTCEPTION_SOURCEMAP_URL="http://localhost:3000/main.js.map"

export TESTCEPTION_MAIN_SCRIPT_REGEX='(?:^|/)main\.js(?:\?.*)?$'
export TESTCEPTION_DANTE_INCLUDE_REGEX='^http://localhost:3000/main\.js(?:\?.*)?$'
export TESTCEPTION_RAW_INCLUDE_REGEX='^http://localhost:3000/.*\.js(?:\?.*)?$'

export TESTCEPTION_APP_SOURCE_INCLUDE_REGEX='(?:^|/)(?:src|app)/'
export TESTCEPTION_APP_SOURCE_EXCLUDE_REGEX='(?:^|/)(?:node_modules|vendor|webpack)(?:/|$)'

export TESTCEPTION_DANTE_SRC_CODE_FOLDER='src'
export TESTCEPTION_DANTE_SRC_CODE_FILES_TO_EXCLUDE=''
export TESTCEPTION_DANTE_EMPTY_EXCLUSION_BEHAVIOR='java'

mkdir -p "$LOG_DIR"

# ------------------------------------------------------------
# Compile Selenium4 suite
# ------------------------------------------------------------

step "Compiling Selenium4 test suite"

cd "$SUITE"
mvn -q \
    -Dmaven.repo.local="$MAVEN_REPO" \
    test-compile

# ------------------------------------------------------------
# Three coverage metrics
# ------------------------------------------------------------

step "Running Testception coverage experiment"

echo "This execution produces:"
echo
echo "  1. DANTE-compatible coverage"
echo "  2. Modern application coverage"
echo "  3. Raw bundle coverage"
echo

set +e

./scripts/run-three-coverage.sh \
    2>&1 |
    tee "$LOG_FILE"

RUN_STATUS=${PIPESTATUS[0]}

set -e

if [[ "$RUN_STATUS" -ne 0 ]]; then
    echo
    echo "Testception execution FAILED."
    echo
    echo "Log:"
    echo "  $LOG_FILE"
    exit "$RUN_STATUS"
fi

# ------------------------------------------------------------
# Final summary
# ------------------------------------------------------------

RESULT_DIR="$COVERAGE_DIR/$APP/selenium4-three-metrics"
SUMMARY="$RESULT_DIR/coverage-summary.md"

step "Testception completed successfully"

echo "Mode: $MODE"
echo

if [[ -f "$SUMMARY" ]]; then
    cat "$SUMMARY"
else
    echo "Coverage summary file not found."
fi

echo
echo "Results:"
echo "  $RESULT_DIR"
echo
echo "Full log:"
echo "  $LOG_FILE"
echo
