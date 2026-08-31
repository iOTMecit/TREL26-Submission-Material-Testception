#!/usr/bin/env bash
set -Eeuo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP="${1:-}"
step(){ echo; echo "============================================================"; echo " $1"; echo "============================================================"; }
fail(){ echo; echo "ERROR: $1"; exit 1; }
[[ -n "$APP" ]] || fail "Usage: ./run-global-baseline.sh <application> [--headless|--headed] [--reuse-plan]"
shift
HEADLESS="false"
REUSE_PLAN="false"
while [[ $# -gt 0 ]]; do
    case "$1" in
        --headless) HEADLESS="true" ;;
        --headed) HEADLESS="false" ;;
        --reuse-plan) REUSE_PLAN="true" ;;
        *) fail "Unknown option: $1" ;;
    esac
    shift
done

DANTE_DIR="$ROOT/dante"

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

RLM="$ROOT/Testception/rlm_project"
PLANNER="$RLM/global_project_planner_strict_parity.py"
VENV="$RLM/.venv-selenium4"
REQ="$ROOT/requirements.txt"
BRIDGE="$ROOT/scripts/prepare-selenium4-suite.py"
SUITE_STRATEGY="$(suite_strategy_for_app "$APP")"
BASE_SUITE="$DANTE_DIR/applications/$APP/testsuite-$APP-selenium4"
GLOBAL_LEGACY="$DANTE_DIR/applications/$APP/testsuite-$APP-global/src/main/java/tests/GeneratedTestSuite${SUITE_STRATEGY}.java"
GLOBAL_SUITE="$DANTE_DIR/applications/$APP/testsuite-$APP-selenium4-global"
GLOBAL_TEST="$GLOBAL_SUITE/src/test/java/tests/GeneratedTestSuiteFiredTest.java"
CRAWL="$DANTE_DIR/applications/$APP/localhost/crawl-with-inputs"
M2="$ROOT/.m2-selenium4/repository"
COV="$ROOT/coverage-results"
LOG="$ROOT/logs/${APP}-global-one-shot-three-coverage.log"

step "Checking same-crawl global baseline"
[[ -f "$CRAWL/result.json" || -f "$CRAWL/crawl0/result.json" ]] || fail "No existing crawl. Run recursive --full first."
[[ -d "$BASE_SUITE" ]] || fail "Base Selenium4 suite missing: $BASE_SUITE"
[[ -f "$PLANNER" ]] || fail "Global planner missing: $PLANNER"
[[ -f "$BRIDGE" ]] || fail "Bridge missing: $BRIDGE"
[[ -f "$REQ" ]] || fail "requirements.txt missing: $REQ"
if [[ "$REUSE_PLAN" != "true" ]]; then
    [[ -n "${TESTCEPTION_LLM_MODEL:-}" ]] || fail "TESTCEPTION_LLM_MODEL is not set."
    [[ -n "${OPENROUTER_API_KEY:-}" ]] || fail "OPENROUTER_API_KEY is not set."
fi

echo "Application: $APP"
echo "Strategy   : $SUITE_STRATEGY"
echo "Crawl      : REUSED; no new crawling will occur."

step "Preparing Python environment"
[[ -d "$VENV" ]] || python3 -m venv "$VENV"
# shellcheck disable=SC1091
source "$VENV/bin/activate"
python -m pip install --disable-pip-version-check -r "$REQ"

if [[ "$REUSE_PLAN" == "true" ]]; then
    step "Revalidating the existing one-shot global plan"
    python "$PLANNER" "$APP" --reuse-plan
else
    step "One global LLM planning request"
    python "$PLANNER" "$APP"
fi
[[ -f "$GLOBAL_LEGACY" ]] || fail "Global legacy suite not generated: $GLOBAL_LEGACY"
LEGACY_COUNT="$(grep -Ec 'public void test[0-9]{3}\(' "$GLOBAL_LEGACY" || true)"
[[ "$LEGACY_COUNT" -gt 0 ]] || fail "Global suite has zero tests."

step "Materializing with the SAME Selenium4 bridge"
rm -rf "$GLOBAL_SUITE"
cp -a "$BASE_SUITE" "$GLOBAL_SUITE"
rm -rf "$GLOBAL_SUITE/target"
python "$BRIDGE" "$GLOBAL_LEGACY" "$GLOBAL_SUITE"
SEL_COUNT="$(grep -Ec 'public void test[0-9]{3}\(' "$GLOBAL_TEST" || true)"
[[ "$LEGACY_COUNT" -eq "$SEL_COUNT" ]] || fail "Legacy/Selenium4 count mismatch."
echo "Global tests: $SEL_COUNT"

step "Java 17"
JAVA17_HOME="${JAVA17_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}"
[[ -x "$JAVA17_HOME/bin/java" ]] || fail "Java 17 not found at $JAVA17_HOME"
export JAVA_HOME="$JAVA17_HOME"
export PATH="$JAVA_HOME/bin:$PATH"
java -version

step "Configuring the SAME application-aware coverage profile"
export TESTCEPTION_TEST_CLASS="tests.GeneratedTestSuiteFiredTest"
export TESTCEPTION_COVERAGE_DIR="$COV"
export TESTCEPTION_COVERAGE_TECHNIQUE="global-one-shot-three-metrics"
export TESTCEPTION_HEADLESS="$HEADLESS"
export TESTCEPTION_BROWSER_RECYCLE_EVERY="${TESTCEPTION_BROWSER_RECYCLE_EVERY:-1}"
mkdir -p "$ROOT/logs"

if ! export_coverage_profile "$GLOBAL_SUITE"; then
    print_coverage_profile_unavailable
    step "Global baseline planning/generation completed"
    echo "Planner metrics: $ROOT/global-baseline-results/$APP/global-planner-metrics.json"
    echo "Generated suite : $GLOBAL_TEST"
    echo "Coverage        : skipped (no source-map profile)"
    exit 0
fi

echo "App URL     : $APP_URL"
echo "Main script : $SCRIPT_NAME"
echo "Sourcemap   : $SOURCEMAP_URL"
echo "Source root : $SRC_FOLDER"
echo "Reset       : $RESET_COMMAND"

cd "$GLOBAL_SUITE"
mvn -q -Dmaven.repo.local="$M2" test-compile
set +e
./scripts/run-three-coverage.sh 2>&1 | tee "$LOG"
STATUS=${PIPESTATUS[0]}
set -e
[[ "$STATUS" -eq 0 ]] || fail "Global coverage failed. Log: $LOG"

step "Global baseline complete"
echo "Planner metrics: $ROOT/global-baseline-results/$APP/global-planner-metrics.json"
echo "Coverage       : $COV/$APP/global-one-shot-three-metrics"
echo "Log            : $LOG"
