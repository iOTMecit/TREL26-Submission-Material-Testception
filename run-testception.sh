#!/usr/bin/env bash

set -Eeuo pipefail

# ============================================================
# Testception - One Command Runner
#
# Usage:
#   ./run-testception.sh petclinic
#
# Optional:
#   ./run-testception.sh petclinic --headless
# ============================================================


# ------------------------------------------------------------
# Resolve repository root
# ------------------------------------------------------------

ROOT_DIR="$(
    cd "$(dirname "${BASH_SOURCE[0]}")" &&
    pwd
)"

APP="${1:-petclinic}"
shift || true


# ------------------------------------------------------------
# Options
# ------------------------------------------------------------

HEADLESS="false"

while [[ $# -gt 0 ]]; do
    case "$1" in
        --headless)
            HEADLESS="true"
            shift
            ;;

        --headed)
            HEADLESS="false"
            shift
            ;;

        *)
            echo "Unknown option: $1"
            echo
            echo "Usage:"
            echo "  ./run-testception.sh petclinic"
            echo "  ./run-testception.sh petclinic --headless"
            exit 1
            ;;
    esac
done


# ------------------------------------------------------------
# Paths
# ------------------------------------------------------------

SUITE="$ROOT_DIR/dante/applications/$APP/testsuite-$APP-selenium4"

RLM_DIR="$ROOT_DIR/rlm_project"

VENV="$RLM_DIR/.venv-selenium4"

COVERAGE_DIR="$ROOT_DIR/coverage-results"

LOG_DIR="$ROOT_DIR/logs"


# ------------------------------------------------------------
# Pretty output
# ------------------------------------------------------------

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


# ------------------------------------------------------------
# Validate application
# ------------------------------------------------------------

case "$APP" in
    petclinic)
        ;;
    *)
        fail "Currently supported application: petclinic"
        ;;
esac


# ------------------------------------------------------------
# Check system prerequisites
# ------------------------------------------------------------

step "Checking system requirements"

command -v docker >/dev/null 2>&1 ||
    fail "Docker is not installed."

command -v mvn >/dev/null 2>&1 ||
    fail "Maven is not installed."

command -v python3 >/dev/null 2>&1 ||
    fail "Python 3 is not installed."

command -v curl >/dev/null 2>&1 ||
    fail "curl is not installed."

docker info >/dev/null 2>&1 ||
    fail "Docker daemon is not running."

[[ -d "$SUITE" ]] ||
    fail "Selenium4 test suite not found: $SUITE"

[[ -x "$SUITE/scripts/run-three-coverage.sh" ]] ||
    fail "run-three-coverage.sh not found or not executable."

echo "Repository : $ROOT_DIR"
echo "Application: $APP"
echo "Headless   : $HEADLESS"


# ------------------------------------------------------------
# LLM configuration
# ------------------------------------------------------------

step "Checking LLM configuration"

if [[ -z "${TESTCEPTION_LLM_MODEL:-}" ]]; then
    fail "TESTCEPTION_LLM_MODEL is not set.

Before running Testception:

  export TESTCEPTION_LLM_MODEL=\"openrouter/openai/gpt-4o-mini\""
fi

if [[ -z "${OPENROUTER_API_KEY:-}" ]]; then
    fail "OPENROUTER_API_KEY is not set.

Before running Testception:

  read -s -p \"OpenRouter API Key: \" OPENROUTER_API_KEY
  echo
  export OPENROUTER_API_KEY"
fi

echo "LLM model: $TESTCEPTION_LLM_MODEL"
echo "OpenRouter API key: configured"

# ------------------------------------------------------------
# Python virtual environment
# ------------------------------------------------------------

step "Preparing Python environment"

if [[ ! -d "$VENV" ]]; then
    echo "Creating virtual environment..."
    python3 -m venv "$VENV"
fi

# shellcheck disable=SC1091
source "$VENV/bin/activate"

python -m pip install \
    --disable-pip-version-check \
    --upgrade pip


# Find requirements automatically.
REQUIREMENTS=""

for candidate in \
    "$RLM_DIR/requirements.txt" \
    "$ROOT_DIR/requirements.txt"
do
    if [[ -f "$candidate" ]]; then
        REQUIREMENTS="$candidate"
        break
    fi
done

if [[ -n "$REQUIREMENTS" ]]; then
    echo "Installing Python dependencies from:"
    echo "  $REQUIREMENTS"

    python -m pip install \
        --disable-pip-version-check \
        -r "$REQUIREMENTS"
else
    echo "No requirements.txt found; continuing."
fi


# ------------------------------------------------------------
# Java 17
# ------------------------------------------------------------

step "Preparing Java 17"

JAVA17_HOME="${JAVA17_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}"

[[ -x "$JAVA17_HOME/bin/java" ]] ||
    fail "Java 17 not found at $JAVA17_HOME"

export JAVA_HOME="$JAVA17_HOME"
export PATH="$JAVA_HOME/bin:$PATH"

java -version

echo
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


# ------------------------------------------------------------
# Petclinic
# ------------------------------------------------------------

export TESTCEPTION_RESET_COMMAND="$SUITE/scripts/reset-petclinic.sh"

export TESTCEPTION_WAIT_URL="http://localhost:3000/"

export TESTCEPTION_SOURCEMAP_URL="http://localhost:3000/main.js.map"


# ------------------------------------------------------------
# Script selection
# ------------------------------------------------------------

export TESTCEPTION_MAIN_SCRIPT_REGEX='(?:^|/)main\.js(?:\?.*)?$'

export TESTCEPTION_DANTE_INCLUDE_REGEX='^http://localhost:3000/main\.js(?:\?.*)?$'

export TESTCEPTION_RAW_INCLUDE_REGEX='^http://localhost:3000/.*\.js(?:\?.*)?$'


# ------------------------------------------------------------
# Modern application coverage
# ------------------------------------------------------------

export TESTCEPTION_APP_SOURCE_INCLUDE_REGEX='(?:^|/)(?:src|app)/'

export TESTCEPTION_APP_SOURCE_EXCLUDE_REGEX='(?:^|/)(?:node_modules|vendor|webpack)(?:/|$)'


# ------------------------------------------------------------
# DANTE historical compatibility
# ------------------------------------------------------------

export TESTCEPTION_DANTE_SRC_CODE_FOLDER='src'

export TESTCEPTION_DANTE_SRC_CODE_FILES_TO_EXCLUDE=''

export TESTCEPTION_DANTE_EMPTY_EXCLUSION_BEHAVIOR='java'


# ------------------------------------------------------------
# Logs
# ------------------------------------------------------------

mkdir -p "$LOG_DIR"

LOG_FILE="$LOG_DIR/${APP}-three-coverage.log"


# ------------------------------------------------------------
# Compile before execution
# ------------------------------------------------------------

step "Compiling Selenium4 test suite"

cd "$SUITE"

mvn \
    -q \
    test-compile


# ------------------------------------------------------------
# Execute
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


# ------------------------------------------------------------
# Check result
# ------------------------------------------------------------

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
