#!/usr/bin/env bash
set -Eeuo pipefail

# Testception - unified all-application runner
# Supported: petclinic, dimeshift, splittypie, retroboard, phoenix, ecommerce
#
#   ./run-testception.sh <application> --full [--headless|--headed]
#   ./run-testception.sh <application> --coverage [--headless|--headed]
#
# --full: fresh crawl -> Recursive LLM -> Java generation -> Selenium4 bridge
#         -> three coverage metrics when the app has a source-map profile.
# --coverage: reruns the existing Selenium4 suite only.

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP="${1:-}"

step(){ echo; echo "============================================================"; echo " $1"; echo "============================================================"; }
fail(){ echo; echo "ERROR: $1"; exit 1; }

[[ -n "$APP" ]] || fail "Usage: ./run-testception.sh <application> (--full|--coverage) [--headless|--headed]"
shift

MODE=""
HEADLESS="false"
PROGRESS="false"
FAST="false"
# TESTCEPTION_RUNTIME_UX_V7
while [[ $# -gt 0 ]]; do
    case "$1" in
        --full) [[ -z "$MODE" ]] || fail "Choose only one mode."; MODE="full" ;;
        --coverage) [[ -z "$MODE" ]] || fail "Choose only one mode."; MODE="coverage" ;;
        --headless) HEADLESS="true" ;;
        --headed) HEADLESS="false" ;;
        --progress) PROGRESS="true" ;;
        --fast) FAST="true" ;;
        *) fail "Unknown option: $1" ;;
    esac
    shift
done
[[ -n "$MODE" ]] || fail "Execution mode is required: --full or --coverage"

# TESTCEPTION_RUNTIME_UX_V7
PIPELINE_STARTED_AT="$(date +%s)"
LLM_DURATION_SECONDS=""
TEST_EXECUTION_DURATION_SECONDS=""

format_testception_duration() {
    local total="${1:-0}"
    local h=$(( total / 3600 ))
    local m=$(( (total % 3600) / 60 ))
    local s=$(( total % 60 ))
    printf "%02d:%02d:%02d" "$h" "$m" "$s"
}

if [[ "$FAST" == "true" ]]; then
    export TESTCEPTION_WAIT_SCALE="${TESTCEPTION_WAIT_SCALE:-0.20}"
    export TESTCEPTION_VISUAL_HIGHLIGHT="${TESTCEPTION_VISUAL_HIGHLIGHT:-false}"
else
    export TESTCEPTION_WAIT_SCALE="${TESTCEPTION_WAIT_SCALE:-1.0}"
    export TESTCEPTION_VISUAL_HIGHLIGHT="${TESTCEPTION_VISUAL_HIGHLIGHT:-true}"
fi

export TESTCEPTION_PROGRESS_MODE="$PROGRESS"


SUPPORTED_APPS="petclinic dimeshift splittypie retroboard phoenix ecommerce"

validate_app() {
    case "$APP" in
        petclinic|dimeshift|splittypie|retroboard|phoenix|ecommerce)
            ;;
        *)
            fail "Unsupported application: $APP
Supported applications: $SUPPORTED_APPS"
            ;;
    esac
}

suite_strategy_for_app() {
    case "$1" in
        phoenix|splittypie) printf '%s' "Checked" ;;
        *) printf '%s' "Fired" ;;
    esac
}

read_property() {
    local file="$1"
    local key="$2"
    [[ -f "$file" ]] || return 0
    sed -n "s/^[[:space:]]*${key}[[:space:]]*=[[:space:]]*//p" "$file" \
        | tail -n 1 \
        | tr -d '\r'
}

fallback_app_url() {
    case "$1" in
        petclinic|dimeshift) printf '%s' "http://localhost:3000" ;;
        phoenix|retroboard) printf '%s' "http://localhost:4000" ;;
        splittypie) printf '%s' "http://localhost:4200" ;;
        ecommerce) printf '%s' "" ;;
    esac
}

load_coverage_profile() {
    local suite_root="$1"
    local legacy_props="$DANTE_DIR/applications/$APP/testsuite-$APP/src/main/resources/app.properties"

    local app_url=""
    local script_name=""
    local sourcemap_url=""
    local src_folder=""
    local src_excludes=""

    app_url="$(read_property "$legacy_props" app_url)"
    script_name="$(read_property "$legacy_props" script_name_to_include)"
    sourcemap_url="$(read_property "$legacy_props" sourcemap_url)"
    src_folder="$(read_property "$legacy_props" src_code_folder)"
    src_excludes="$(read_property "$legacy_props" src_code_files_to_exclude)"

    [[ -n "$app_url" ]] || app_url="$(fallback_app_url "$APP")"

    # Preserve the already validated Petclinic profile exactly.
    if [[ "$APP" == "petclinic" ]]; then
        app_url="http://localhost:3000"
        script_name="main"
        sourcemap_url="http://localhost:3000/main.js.map"
        src_folder="src"
        src_excludes=""
    fi

    # Known legacy profiles are used only when the checked-in app.properties
    # is missing or incomplete. They mirror the DANTE subject configuration.
    case "$APP" in
        phoenix)
            [[ -n "$app_url" ]] || app_url="http://localhost:4000"
            [[ -n "$script_name" ]] || script_name="application"
            [[ -n "$sourcemap_url" ]] || sourcemap_url="http://localhost:4000/js/application.js.map"
            [[ -n "$src_folder" ]] || src_folder="web/static/js"
            ;;
        retroboard)
            [[ -n "$app_url" ]] || app_url="http://localhost:4000"
            [[ -n "$script_name" ]] || script_name="app.0.10.0"
            [[ -n "$sourcemap_url" ]] || sourcemap_url="http://localhost:4000/assets/app.0.10.0.js.map"
            [[ -n "$src_folder" ]] || src_folder="app"
            ;;
        splittypie)
            [[ -n "$app_url" ]] || app_url="http://localhost:4200"
            [[ -n "$script_name" ]] || script_name="splittypie"
            [[ -n "$sourcemap_url" ]] || sourcemap_url="http://localhost:4200/assets/splittypie.map"
            [[ -n "$src_folder" ]] || src_folder="splittypie"
            ;;
    esac

    # Explicit environment overrides always win. This is useful for a local
    # build whose ports or bundle names differ from the checked-in DANTE data.
    app_url="${TESTCEPTION_APP_URL:-$app_url}"
    script_name="${TESTCEPTION_SCRIPT_NAME:-$script_name}"
    sourcemap_url="${TESTCEPTION_SOURCEMAP_URL:-$sourcemap_url}"
    src_folder="${TESTCEPTION_DANTE_SRC_CODE_FOLDER:-$src_folder}"
    src_excludes="${TESTCEPTION_DANTE_SRC_CODE_FILES_TO_EXCLUDE:-$src_excludes}"

    [[ -n "$app_url" ]] || fail "Could not determine app_url for '$APP'.
Set TESTCEPTION_APP_URL explicitly or restore the application's app.properties."

    APP_URL="${app_url%/}"
    WAIT_URL="${TESTCEPTION_WAIT_URL:-${APP_URL}/}"
    SOURCEMAP_URL="$sourcemap_url"
    SRC_FOLDER="$src_folder"
    SRC_EXCLUDES="$src_excludes"
    SCRIPT_NAME="$script_name"

    # TESTCEPTION_DIMESHIFT_LEGACY_MULTISCRIPT_V1
    COVERAGE_PROFILE_MODE="sourcemap"

    if [[ "$APP" == "dimeshift" ]]; then
        COVERAGE_PROFILE_MODE="legacy-multiscript"
        SCRIPT_NAME="legacy-multiscript"
        SOURCEMAP_URL=""
        SRC_FOLDER="scripts"
        SRC_EXCLUDES=""

        local origin_regex="${APP_URL//./\\.}"

        # DANTE collector intentionally captures a JS superset here.
        # The dedicated postprocessor applies original CodeCoverage.ignore().
        DANTE_INCLUDE_REGEX="${TESTCEPTION_DANTE_INCLUDE_REGEX:-^${origin_regex}/.*\.js(?:\?.*)?$}"
        RAW_INCLUDE_REGEX="${TESTCEPTION_RAW_INCLUDE_REGEX:-^${origin_regex}/.*\\.js(?:\\?.*)?$}"
        APP_SOURCE_INCLUDE_REGEX="${TESTCEPTION_APP_SOURCE_INCLUDE_REGEX:-^${origin_regex}/scripts/.*\\.js(?:\\?.*)?$}"
        APP_SOURCE_EXCLUDE_REGEX="${TESTCEPTION_APP_SOURCE_EXCLUDE_REGEX:-(?!)}"
        MAIN_SCRIPT_REGEX=""

        THREE_METRIC_AVAILABLE="true"
        return 0
    fi

    # The current three-metric postprocessor needs one selected application
    # bundle plus a source map. DANTE subjects configured only through an
    # exclusion list (notably the historical Dimeshift profile) are still
    # supported for crawling/RLM/Selenium4 suite generation, but we refuse to
    # invent a Modern-Application denominator for them.
    THREE_METRIC_AVAILABLE="true"
    if [[ -z "$SCRIPT_NAME" || -z "$SOURCEMAP_URL" || -z "$SRC_FOLDER" ]]; then
        THREE_METRIC_AVAILABLE="false"
        return 0
    fi

    local script_file="$SCRIPT_NAME"
    [[ "$script_file" == *.js ]] || script_file="${script_file}.js"

    local script_regex="${script_file//./\\.}"
    local origin_regex="${APP_URL//./\\.}"
    local src_regex="${SRC_FOLDER//./\\.}"

    MAIN_SCRIPT_REGEX="(?:^|/)${script_regex}(?:\\?.*)?$"
    DANTE_INCLUDE_REGEX="^${origin_regex}/(?:.*/)?${script_regex}(?:\\?.*)?$"
    RAW_INCLUDE_REGEX="^${origin_regex}/.*\\.js(?:\\?.*)?$"

    if [[ "$APP" == "petclinic" ]]; then
        APP_SOURCE_INCLUDE_REGEX='(?:^|/)(?:src|app)/'
    else
        APP_SOURCE_INCLUDE_REGEX="(?:^|/)${src_regex%/}/"
    fi
    APP_SOURCE_EXCLUDE_REGEX='(?:^|/)(?:node_modules|vendor|webpack)(?:/|$)'
}

write_generic_reset_script() {
    local suite_root="$1"
    local reset_script="$suite_root/scripts/reset-testception-app.sh"
    mkdir -p "$suite_root/scripts"

    cat > "$reset_script" <<'RESET_EOF'
#!/usr/bin/env bash
set -Eeuo pipefail

APP="${TESTCEPTION_APP:?TESTCEPTION_APP is required}"
WAIT_URL="${TESTCEPTION_WAIT_URL:?TESTCEPTION_WAIT_URL is required}"
SUITE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ROOT_DIR="$(cd "$SUITE_DIR/../../../.." && pwd)"
DOCKER_DIR="$ROOT_DIR/dante/docker/$APP"

[[ -d "$DOCKER_DIR" ]] || {
    echo "ERROR: Docker directory not found: $DOCKER_DIR" >&2
    exit 1
}
[[ -x "$DOCKER_DIR/run-docker.sh" ]] || {
    echo "ERROR: run-docker.sh is not executable: $DOCKER_DIR/run-docker.sh" >&2
    exit 1
}

ids="$(docker ps -a --format '{{.ID}} {{.Names}} {{.Image}}' \
    | grep -i -- "$APP" \
    | awk '{print $1}' \
    | sort -u || true)"
if [[ -n "$ids" ]]; then
    # shellcheck disable=SC2086
    docker rm -f $ids >/dev/null 2>&1 || true
fi

cd "$DOCKER_DIR"
./run-docker.sh -p yes -n "${APP}-testception-coverage"

for _ in $(seq 1 90); do
    if curl -kfsS "$WAIT_URL" >/dev/null 2>&1; then
        echo "TESTCEPTION_APP_READY: $WAIT_URL"
        exit 0
    fi
    sleep 2
done

echo "ERROR: Application did not become ready: $WAIT_URL" >&2
exit 1
RESET_EOF
    chmod +x "$reset_script"
    printf '%s' "$reset_script"
}

select_reset_command() {
    local suite_root="$1"

    # Keep the already validated Petclinic reset unchanged.
    if [[ "$APP" == "petclinic" && -x "$suite_root/scripts/reset-petclinic.sh" ]]; then
        RESET_COMMAND="$suite_root/scripts/reset-petclinic.sh"
        return 0
    fi

    # If a future app-specific pass-level reset exists, prefer it.
    if [[ -x "$suite_root/scripts/reset-$APP.sh" ]]; then
        RESET_COMMAND="$suite_root/scripts/reset-$APP.sh"
        return 0
    fi

    RESET_COMMAND="$(write_generic_reset_script "$suite_root")"
}

export_coverage_profile() {
    local suite_root="$1"
    load_coverage_profile "$suite_root"
    [[ "$THREE_METRIC_AVAILABLE" == "true" ]] || return 1
    select_reset_command "$suite_root"

    export TESTCEPTION_SUITE_DIR="$suite_root"
    export TESTCEPTION_COVERAGE_PROFILE_MODE="${COVERAGE_PROFILE_MODE:-sourcemap}"
    export TESTCEPTION_APP="$APP"
    export TESTCEPTION_APP_URL="$APP_URL"
    export TESTCEPTION_RESET_COMMAND="$RESET_COMMAND"
    export TESTCEPTION_WAIT_URL="$WAIT_URL"
    export TESTCEPTION_SOURCEMAP_URL="$SOURCEMAP_URL"
    export TESTCEPTION_MAIN_SCRIPT_REGEX="$MAIN_SCRIPT_REGEX"
    export TESTCEPTION_DANTE_INCLUDE_REGEX="$DANTE_INCLUDE_REGEX"
    export TESTCEPTION_RAW_INCLUDE_REGEX="$RAW_INCLUDE_REGEX"
    export TESTCEPTION_APP_SOURCE_INCLUDE_REGEX="$APP_SOURCE_INCLUDE_REGEX"
    export TESTCEPTION_APP_SOURCE_EXCLUDE_REGEX="$APP_SOURCE_EXCLUDE_REGEX"
    export TESTCEPTION_DANTE_SRC_CODE_FOLDER="$SRC_FOLDER"
    export TESTCEPTION_DANTE_SRC_CODE_FILES_TO_EXCLUDE="$SRC_EXCLUDES"
    export TESTCEPTION_DANTE_EMPTY_EXCLUSION_BEHAVIOR="${TESTCEPTION_DANTE_EMPTY_EXCLUSION_BEHAVIOR:-java}"
}

print_coverage_profile_unavailable() {
    echo
    echo "Three-metric coverage is not configured for '$APP' in the checked-in DANTE profile."
    echo "The crawl/RLM/Selenium4 suite is still supported and has been generated."
    echo
    echo "The current Modern Application metric requires:"
    echo "  - one application script_name_to_include"
    echo "  - a sourcemap_url"
    echo "  - src_code_folder"
    echo
    echo "You can provide a local build profile with:"
    echo "  TESTCEPTION_APP_URL=..."
    echo "  TESTCEPTION_SCRIPT_NAME=..."
    echo "  TESTCEPTION_SOURCEMAP_URL=..."
    echo "  TESTCEPTION_DANTE_SRC_CODE_FOLDER=..."
}

validate_app

DANTE_DIR="$ROOT_DIR/dante"
RLM_DIR="$ROOT_DIR/Testception/rlm_project"
VENV="$RLM_DIR/.venv-selenium4"
REQUIREMENTS="$ROOT_DIR/requirements.txt"
MAVEN_REPO="$ROOT_DIR/.m2-selenium4/repository"
COVERAGE_DIR="$ROOT_DIR/coverage-results"
LOG_DIR="$ROOT_DIR/logs"
LOG_FILE="$LOG_DIR/${APP}-${MODE}-three-coverage.log"
BRIDGE_SCRIPT="$ROOT_DIR/scripts/prepare-selenium4-suite.py"

SUITE_STRATEGY="$(suite_strategy_for_app "$APP")"
SUITE="$DANTE_DIR/applications/$APP/testsuite-$APP-selenium4"
SELENIUM4_TEST_FILE="$SUITE/src/test/java/tests/GeneratedTestSuiteFiredTest.java"
LEGACY_GENERATED_TEST="$DANTE_DIR/applications/$APP/testsuite-$APP/src/main/java/tests/GeneratedTestSuite${SUITE_STRATEGY}.java"
CRAWL_PARENT="$DANTE_DIR/applications/$APP/localhost"
CRAWL_DIR="$CRAWL_PARENT/crawl-with-inputs"
CRAWL_MINUTES="${TESTCEPTION_CRAWL_MINUTES:-10}"

bootstrap_selenium4_harness_if_needed() {
    [[ -d "$SUITE" ]] && return 0
    [[ "$MODE" == "full" ]] || fail "Selenium4 suite not found: $SUITE"

    local template="$DANTE_DIR/applications/petclinic/testsuite-petclinic-selenium4"
    [[ "$APP" != "petclinic" ]] || fail "Petclinic Selenium4 template itself is missing: $template"
    [[ -d "$template" ]] || fail "Validated Selenium4 harness template not found: $template"

    step "Bootstrapping Selenium4 harness for $APP"
    mkdir -p "$(dirname "$SUITE")"
    cp -a "$template" "$SUITE"
    rm -rf "$SUITE/target"
    rm -f "$SELENIUM4_TEST_FILE"
    echo "Harness copied from the validated Petclinic Selenium4 execution layer."
    echo "Application-specific generated tests and coverage environment remain dynamic."
}

step "Checking system requirements"
command -v docker >/dev/null 2>&1 || fail "Docker is not installed."
command -v mvn >/dev/null 2>&1 || fail "Maven is not installed."
command -v python3 >/dev/null 2>&1 || fail "Python 3 is not installed."
command -v curl >/dev/null 2>&1 || fail "curl is not installed."
docker info >/dev/null 2>&1 || fail "Docker daemon is not running."
[[ -d "$DANTE_DIR" ]] || fail "DANTE directory not found: $DANTE_DIR"
bootstrap_selenium4_harness_if_needed
[[ -x "$SUITE/scripts/run-three-coverage.sh" ]] || fail "run-three-coverage.sh missing: $SUITE/scripts/run-three-coverage.sh"

if [[ "$MODE" == "full" ]]; then
    [[ -f "$RLM_DIR/mentor_orchestrator.py" ]] || fail "mentor_orchestrator.py missing."
    [[ -f "$DANTE_DIR/run-crawling.sh" ]] || fail "run-crawling.sh missing."
    [[ -f "$BRIDGE_SCRIPT" ]] || fail "Selenium4 bridge missing."
fi

echo "Repository : $ROOT_DIR"
echo "Application: $APP"
echo "Strategy   : $SUITE_STRATEGY"
echo "Mode       : $MODE"
echo "Headless   : $HEADLESS"

if [[ "$MODE" == "full" ]]; then
    step "Checking LLM configuration"
    [[ -n "${TESTCEPTION_LLM_MODEL:-}" ]] || fail "TESTCEPTION_LLM_MODEL is not set."
    [[ -n "${OPENROUTER_API_KEY:-}" ]] || fail "OPENROUTER_API_KEY is not set."
    echo "LLM model: $TESTCEPTION_LLM_MODEL"

    step "Preparing Python RLM environment"
    [[ -f "$REQUIREMENTS" ]] || fail "requirements.txt not found: $REQUIREMENTS"
    [[ -d "$VENV" ]] || python3 -m venv "$VENV"
    # shellcheck disable=SC1091
    source "$VENV/bin/activate"
    python -m pip install --disable-pip-version-check -r "$REQUIREMENTS"

    step "Preparing Java 8 for Crawljax and DANTE"
    JAVA8_HOME="${JAVA8_HOME:-/usr/lib/jvm/java-8-openjdk-amd64}"
    [[ -x "$JAVA8_HOME/bin/java" ]] || fail "Java 8 not found at $JAVA8_HOME"
    [[ -x "$JAVA8_HOME/bin/javac" ]] || fail "Java 8 javac not found at $JAVA8_HOME"
    export JAVA_HOME="$JAVA8_HOME"
    export PATH="$JAVA_HOME/bin:$PATH"
    export TESTCEPTION_MAVEN_REPO="$MAVEN_REPO"
    mkdir -p "$MAVEN_REPO" "$LOG_DIR"

    step "Building legacy Crawljax"
    cd "$ROOT_DIR/crawljax"
    mvn -q -Dmaven.repo.local="$MAVEN_REPO" -DskipTests install

    step "Building legacy DANTE"
    cd "$DANTE_DIR"
    mvn -q -Dmaven.repo.local="$MAVEN_REPO" -DskipTests install

    step "Cleaning previous Crawljax output"
    rm -rf "$CRAWL_PARENT/crawl0" "$CRAWL_DIR"
    export TESTCEPTION_LOG_DIR="$LOG_DIR"

    step "Running fresh Crawljax exploration"
    cd "$DANTE_DIR"
    ./run-crawling.sh "$APP" "$HEADLESS" "$CRAWL_MINUTES"
    [[ -f "$CRAWL_DIR/result.json" ]] || fail "Fresh result.json not generated: $CRAWL_DIR/result.json"
    [[ -d "$CRAWL_DIR/doms" ]] || fail "Fresh DOM directory not generated: $CRAWL_DIR/doms"

    step "Running Recursive LLM test generation"
    cd "$RLM_DIR"
    LLM_STARTED_AT="$(date +%s)"
    python mentor_orchestrator.py "$APP"
    LLM_DURATION_SECONDS="$(( $(date +%s) - LLM_STARTED_AT ))"
    echo "LLM scenario generation time: $(format_testception_duration "$LLM_DURATION_SECONDS")"
    [[ -f "$LEGACY_GENERATED_TEST" ]] || fail "RLM suite not found: $LEGACY_GENERATED_TEST"
    LEGACY_TEST_COUNT="$(grep -Ec 'public void test[0-9]{3}\(' "$LEGACY_GENERATED_TEST" || true)"
    [[ "$LEGACY_TEST_COUNT" -gt 0 ]] || fail "RLM generated zero Java tests."

    step "Preparing Selenium4 suite from RLM output"
    python "$BRIDGE_SCRIPT" "$LEGACY_GENERATED_TEST" "$SUITE"
    [[ -f "$SELENIUM4_TEST_FILE" ]] || fail "Selenium4 test file not found: $SELENIUM4_TEST_FILE"
    SELENIUM4_TEST_COUNT="$(grep -Ec 'public void test[0-9]{3}\(' "$SELENIUM4_TEST_FILE" || true)"
    [[ "$SELENIUM4_TEST_COUNT" -eq "$LEGACY_TEST_COUNT" ]] || fail "Test count mismatch: legacy=$LEGACY_TEST_COUNT selenium4=$SELENIUM4_TEST_COUNT"
    echo "Generated Selenium4 tests: $SELENIUM4_TEST_COUNT"
else
    step "Coverage-only mode"
    [[ -f "$SELENIUM4_TEST_FILE" ]] || fail "Existing Selenium4 generated suite not found: $SELENIUM4_TEST_FILE"
    SELENIUM4_TEST_COUNT="$(grep -Ec 'public void test[0-9]{3}\(' "$SELENIUM4_TEST_FILE" || true)"
    [[ "$SELENIUM4_TEST_COUNT" -gt 0 ]] || fail "Existing Selenium4 suite contains zero tests."
    echo "Existing Selenium4 tests: $SELENIUM4_TEST_COUNT"
fi


step "Applying adaptive Selenium4 runtime"
python3 "$ROOT_DIR/scripts/optimize-generated-selenium4.py" \
    "$SELENIUM4_TEST_FILE"

echo "Wait scale : $TESTCEPTION_WAIT_SCALE"
echo "Highlight  : $TESTCEPTION_VISUAL_HIGHLIGHT"
echo "Console    : $([[ "$PROGRESS" == "true" ]] && echo "progress" || echo "verbose")"

step "Preparing Java 17"
JAVA17_HOME="${JAVA17_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}"
[[ -x "$JAVA17_HOME/bin/java" ]] || fail "Java 17 not found at $JAVA17_HOME"
[[ -x "$JAVA17_HOME/bin/javac" ]] || fail "Java 17 javac not found at $JAVA17_HOME"
export JAVA_HOME="$JAVA17_HOME"
export PATH="$JAVA_HOME/bin:$PATH"
java -version

step "Configuring application-aware coverage"
export TESTCEPTION_TEST_CLASS="tests.GeneratedTestSuiteFiredTest"
export TESTCEPTION_COVERAGE_DIR="$COVERAGE_DIR"
export TESTCEPTION_COVERAGE_TECHNIQUE="selenium4-three-metrics"
export TESTCEPTION_HEADLESS="$HEADLESS"
export TESTCEPTION_BROWSER_RECYCLE_EVERY="${TESTCEPTION_BROWSER_RECYCLE_EVERY:-1}"
TESTCEPTION_POSTPROCESS="${TESTCEPTION_POSTPROCESS:-auto}"

case "$TESTCEPTION_POSTPROCESS" in
    auto)
        # Applications reaching run-three-coverage already have a valid
        # three-metric/source-map profile. Run the now-optimized postprocessor
        # so THREE_COVERAGE_RESULTS is produced normally.
        export TESTCEPTION_RUN_POSTPROCESS="true"
        ;;

    always)
        export TESTCEPTION_RUN_POSTPROCESS="true"
        ;;

    never)
        export TESTCEPTION_RUN_POSTPROCESS="false"
        ;;

    *)
        fail "Invalid TESTCEPTION_POSTPROCESS='$TESTCEPTION_POSTPROCESS'. Use: auto, always, never"
        ;;
esac

echo "Postprocess : $TESTCEPTION_RUN_POSTPROCESS ($TESTCEPTION_POSTPROCESS)"
mkdir -p "$LOG_DIR"

# Resolve the application URL even when the full three-metric profile is
# unavailable. This lets every generated Selenium4 suite compile against the
# same application-agnostic APP_URL bridge.
load_coverage_profile "$SUITE"
export TESTCEPTION_APP="$APP"
export TESTCEPTION_APP_URL="$APP_URL"
export TESTCEPTION_SUITE_DIR="$SUITE"

step "Compiling Selenium4 test suite"
cd "$SUITE"
mvn -q -Dmaven.repo.local="$MAVEN_REPO" test-compile
echo "Selenium4 compile: PASS"

if ! export_coverage_profile "$SUITE"; then
    print_coverage_profile_unavailable
    if [[ "$MODE" == "coverage" || "${TESTCEPTION_REQUIRE_THREE_COVERAGE:-false}" == "true" ]]; then
        fail "Three-metric coverage profile unavailable for '$APP'."
    fi
    step "Testception generation completed"
    echo "Application : $APP"
    echo "Generated suite: $SELENIUM4_TEST_FILE"
    echo "Selenium4   : compiled successfully"
    echo "Coverage    : skipped (no source-map profile)"
    exit 0
fi

echo "App URL     : $APP_URL"
echo "Coverage mode: ${COVERAGE_PROFILE_MODE:-sourcemap}"
if [[ "${COVERAGE_PROFILE_MODE:-sourcemap}" == "legacy-multiscript" ]]; then
    echo "DANTE mode  : original Dimeshift multi-script exclusions / bytes"
    echo "Modern app  : direct /scripts/ JavaScript bytes"
else
    echo "Main script : $SCRIPT_NAME"
    echo "Sourcemap   : $SOURCEMAP_URL"
    echo "Source root : $SRC_FOLDER"
fi
echo "Reset       : $RESET_COMMAND"

step "Running Testception coverage experiment"
set +e
TEST_EXECUTION_STARTED_AT="$(date +%s)"

if [[ "$PROGRESS" == "true" ]]; then
    ./scripts/run-three-coverage.sh 2>&1 \
        | tee "$LOG_FILE" \
        | python3 "$ROOT_DIR/scripts/testception-progress.py" \
            "$SELENIUM4_TEST_COUNT"
    RUN_STATUS=${PIPESTATUS[0]}
else
    ./scripts/run-three-coverage.sh 2>&1 \
        | tee "$LOG_FILE"
    RUN_STATUS=${PIPESTATUS[0]}
fi

TEST_EXECUTION_DURATION_SECONDS="$(( $(date +%s) - TEST_EXECUTION_STARTED_AT ))"
set -e
[[ "$RUN_STATUS" -eq 0 ]] || fail "Testception coverage failed. Log: $LOG_FILE"

RESULT_DIR="$COVERAGE_DIR/$APP/selenium4-three-metrics"
SUMMARY="$RESULT_DIR/coverage-summary.md"
step "Testception completed successfully"

PIPELINE_DURATION_SECONDS="$(( $(date +%s) - PIPELINE_STARTED_AT ))"

echo
echo "============================================================"
echo " TESTCEPTION TIMING SUMMARY"
echo "============================================================"
if [[ -n "$LLM_DURATION_SECONDS" ]]; then
    echo "LLM scenario generation : $(format_testception_duration "$LLM_DURATION_SECONDS")"
else
    echo "LLM scenario generation : skipped"
fi
if [[ -n "$TEST_EXECUTION_DURATION_SECONDS" ]]; then
    echo "Generated test execution: $(format_testception_duration "$TEST_EXECUTION_DURATION_SECONDS")"
else
    echo "Generated test execution: skipped"
fi
echo "Total runner time       : $(format_testception_duration "$PIPELINE_DURATION_SECONDS")"
echo "Wait scale              : $TESTCEPTION_WAIT_SCALE"
echo "Console mode            : $([[ "$PROGRESS" == "true" ]] && echo "progress" || echo "verbose")"
echo "============================================================"
echo


if [[ "${TESTCEPTION_RUN_POSTPROCESS:-true}" == "true" ]]; then
    [[ -f "$SUMMARY" ]] && cat "$SUMMARY" || echo "Coverage summary file not found: $SUMMARY"
else
    echo "Coverage postprocess : skipped"
    echo "DANTE raw artifact   : $RESULT_DIR/dante-compatible-run/test-raw-ranges.json"
    echo "Modern/raw artifact  : $RESULT_DIR/modern-raw-run/suite-coverage.json"
fi

echo "Results: $RESULT_DIR"
echo "Log    : $LOG_FILE"
