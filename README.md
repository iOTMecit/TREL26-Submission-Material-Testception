# Testception

Testception is a crawling-based web test generation prototype that combines **Crawljax/DANTE**, a **Recursive LLM (RLM)** planner, and a **Selenium 4 + Chrome DevTools Protocol (CDP)** execution/coverage layer.

The `selenium4-rlm` branch contains the current multi-application pipeline. The legacy Crawljax/DANTE environment is intentionally kept separate from the newer Recursive LLM and Selenium 4 layers so that historical DANTE-compatible measurements can still be produced while using a modern browser automation stack.

Testception has been developed and tested on **Ubuntu 24.04 LTS**.

## Pipeline Overview

```text
Crawljax crawl
    |
    v
result.json + saved DOM states
    |
    v
Recursive Mentor / Worker LLM exploration
    |
    v
Generated DANTE-compatible Java scenarios
    |
    v
Exact-prefix scenario reduction
    |
    v
Selenium 4 suite conversion
    |
    v
Per-test browser-isolated execution
    |
    v
DANTE-compatible + Modern Application + Raw Bundle coverage
```

The current implementation supports the following subject applications used during development/evaluation:

```text
splittypie
petclinic
phoenix
dimeshift
retroboard
```

## Requirements

The current pipeline intentionally uses two Java environments:

```text
Java 8   - legacy Crawljax/DANTE crawling
Java 17  - generated Selenium 4 tests and coverage
```

The development environment used:

```text
/usr/lib/jvm/java-8-openjdk-amd64
/usr/lib/jvm/java-17-openjdk-amd64
```

Also install:

```text
Python 3
python3-venv
pip
Maven 3.x
Google Chrome / Chromium
a compatible ChromeDriver
Docker
```

Docker is used by application reset/start scripts where required.

## Clone the Repository

```bash
git clone \
  --branch selenium4-rlm \
  https://github.com/iOTMecit/Testception.git

cd Testception
```

Verify the branch:

```bash
git branch --show-current
```

Expected:

```text
selenium4-rlm
```

## Python and LLM Configuration

The Recursive LLM implementation is under:

```text
Testception/rlm_project/
```

Main components include:

```text
crawljax_parser.py
mentor_orchestrator.py
worker_agent.py
dante_suite_generator.py
```

Python dependencies are defined in the root-level:

```text
requirements.txt
```

The full runner creates/uses the RLM virtual environment and installs the required dependencies when necessary.

For a full LLM run, set the model explicitly:

```bash
export TESTCEPTION_LLM_MODEL="openrouter/openai/gpt-4o-mini"
```

Set the OpenRouter API key:

```bash
export OPENROUTER_API_KEY="your_key"
```

API keys and model configuration are intentionally **not stored in the repository or in a committed `.env` file**.

Useful RLM/runtime environment variables include:

```bash
# Maximum Worker output token budget.
# Default: 2200
export TESTCEPTION_WORKER_MAX_TOKENS=2200

# Maximum state-local exploration iterations.
# Default: 20
export TESTCEPTION_STATE_ITERATIONS=20

# 0 means no artificial graph-depth limit; exploration continues
# until the available graph/DOM exploration space is exhausted.
export TESTCEPTION_MAX_DEPTH=0
```

## Running Testception

The main entry point is:

```bash
./run-testception.sh
```

The most commonly used command for a complete experiment is:

```bash
./run-testception.sh petclinic --full --fast --progress
```

### Full Pipeline: `--full`

```bash
./run-testception.sh petclinic --full
```

`--full` performs the complete workflow:

1. switches to Java 8;
2. performs a fresh Crawljax crawl through the legacy crawler;
3. loads the newly generated `result.json` and saved DOM states;
4. runs Recursive LLM scenario generation;
5. explores recorded graph transitions and DOM fallback actions;
6. generates a DANTE-compatible Java test suite;
7. removes scenarios that are exact prefixes of longer scenarios;
8. converts the generated suite into the Selenium 4 project;
9. verifies that legacy and Selenium 4 test counts match;
10. switches to Java 17;
11. executes the Selenium 4 suite; and
12. calculates the supported JavaScript coverage metrics.

The legacy crawler is invoked through:

```text
dante/run-crawling.sh
```

### Coverage-Only: `--coverage`

```bash
./run-testception.sh petclinic --coverage
```

`--coverage` skips:

```text
Crawljax
Recursive LLM generation
Java scenario generation
```

and executes the currently available Selenium 4 suite directly.

This mode is useful when:

```text
rerunning an already generated suite
debugging Selenium 4 execution
debugging coverage collection/post-processing
recomputing coverage without spending another LLM run
```

`--coverage` does **not** require an OpenRouter API key or LLM model configuration.

### Progress View: `--progress`

```bash
./run-testception.sh petclinic --full --progress
```

or:

```bash
./run-testception.sh petclinic --coverage --progress
```

`--progress` enables a compact runtime progress view instead of printing every Selenium step to the terminal.

Example:

```text
[################----------------]  50.00% DANTE: 100/200 overall: 100/400
```

Detailed execution output is still preserved in the run log.

`STEP_SKIPPED` messages are intentionally suppressed from the live terminal view when progress mode is active, but they remain available in the log and the final progress summary reports the total skip count.

A completed run prints a summary similar to:

```text
TESTCEPTION_PROGRESS_COMPLETE |
observed_test_executions=398/398 |
step_skipped=... |
elapsed=...
```

### Fast Runtime Mode: `--fast`

```bash
./run-testception.sh petclinic --full --fast
```

`--fast` reduces Selenium-side waiting/highlight overhead to make large generated suites finish faster.

It is intended to reduce runtime overhead; it does **not** reduce the number of generated tests or intentionally skip coverage passes.

A typical full experiment therefore uses:

```bash
./run-testception.sh petclinic \
  --full \
  --fast \
  --progress
```

The fast profile currently uses a reduced Selenium wait scale equivalent to:

```text
TESTCEPTION_WAIT_SCALE=0.20
```

and disables unnecessary visual highlighting during execution.

### Headed / Headless Execution

Headed:

```bash
./run-testception.sh petclinic --full --headed
```

Headless:

```bash
./run-testception.sh petclinic --coverage --headless
```

The flags can be combined:

```bash
./run-testception.sh retroboard \
  --full \
  --fast \
  --progress \
  --headed
```

Headed execution is useful when debugging generated browser interactions.

### Crawl Duration

Override the default crawl duration with:

```bash
TESTCEPTION_CRAWL_MINUTES=15 \
  ./run-testception.sh petclinic --full
```

## Recursive Exploration

A fresh full run creates crawl artifacts under:

```text
dante/applications/<app>/localhost/crawl-with-inputs/
```

Important artifacts include:

```text
result.json
doms/
```

`result.json` contains transitions recorded by Crawljax. Saved DOM states are parsed by the Recursive LLM layer.

Testception uses two complementary exploration modes:

```text
GRAPH_FIRST
DOM_FALLBACK
```

`GRAPH_FIRST` prioritizes currently unconsumed transitions that were actually recorded by Crawljax.

`DOM_FALLBACK` allows the Worker LLM to reason about additional actionable elements present in the current DOM when no useful recorded transition remains.

The current graph-exhaustion strategy is not limited by a small fixed exploration depth. With:

```bash
export TESTCEPTION_MAX_DEPTH=0
```

the Mentor continues until the available recorded graph transitions and relevant DOM exploration opportunities have been consumed.

Recorded-edge injection can also expose a transition from `result.json` to the Worker when the corresponding actionable element is missing from the reduced DOM skeleton. This preserves graph coverage without inventing transitions that were never observed by Crawljax.

## Recursive LLM State and Memory

The Mentor maintains structured exploration state while Worker calls remain independent LLM requests.

The Worker receives bounded context describing:

```text
current state
state-local exploration memory
graph-level exploration memory
recent path context
currently unconsumed recorded transitions
```

This keeps the LLM central to action selection while preventing a continuously growing chat history from becoming the main state mechanism.

## Generated Test Execution

The Selenium 4 bridge is generated/updated through:

```text
scripts/prepare-selenium4-suite.py
```

The generated suite uses Selenium 4 and the same ChromeDriver instance for browser actions and CDP coverage collection.

By default, browser isolation is performed per test:

```text
TESTCEPTION_BROWSER_RECYCLE_EVERY=1
```

The browser recycle implementation creates a replacement ChromeDriver before discarding the previous usable driver and uses a deterministic browser window size instead of a post-start `maximize()` call.

Application-specific backend/database reset hooks can also be injected where required so that stateful applications do not leak data from one generated test into the next.

## ENTER-After-Input Semantics

Some legacy DANTE subject configurations identify input fields that require pressing **Enter** after typing.

The Selenium 4 bridge preserves this configuration generically:

```text
DANTE subject Config.java
        |
        v
inputFieldIdsWithEnterClick
        |
        v
generated step metadata
        |
        v
sendKeys(value)
sendKeys(Keys.ENTER)
```

This behavior is derived from the subject configuration rather than hard-coded for a specific application.

## Selenium 4 Coverage

The Selenium 4 execution layer collects JavaScript coverage from the same ChromeDriver used to perform test actions.

Raw CDP commands are issued through Selenium 4 using:

```text
HasCdp.executeCdpCommand(...)
```

The Selenium 4 Testception path does not use the old cdp4j-based DANTE session.

Coverage normally executes in two browser passes because DANTE-compatible function coverage and modern detailed V8 coverage require different profiler configurations.

### 1. DANTE-Compatible Coverage

This metric reproduces historical DANTE coverage semantics as closely as possible while collecting the raw execution ranges through Selenium 4/CDP.

For applications using the standard bundled profile, the relevant application JavaScript bundle is selected according to the subject coverage configuration.

This metric is intended primarily for comparison with historical DANTE results.

The postprocessor includes validation that rejects impossible results where:

```text
covered > denominator
```

rather than silently clamping a malformed result to 100%.

### 2. Modern Application Coverage

For source-map-enabled applications, this metric maps execution back to application-owned original source lines.

This provides an application-level view that excludes unrelated generated/vendor code where possible.

### 3. Raw Bundle Coverage

Raw generated JavaScript byte coverage over accepted same-origin JavaScript resources.

This is primarily a low-level diagnostic metric.

## Dimeshift Legacy Multi-Script Coverage

Dimeshift differs from the source-map-based applications.

Its historical DANTE configuration uses:

```text
coverage_type=bytes
no source map
multiple JavaScript files
script exclusion rules
```

The current Dimeshift profile therefore uses a dedicated legacy multi-script postprocessor.

For DANTE-compatible Dimeshift coverage:

```text
only subject-origin JavaScript is considered
historical DANTE ignore/exclusion rules are applied
coverage is calculated per accepted script
the historical arithmetic-mean aggregation is preserved
```

The explicit subject-origin boundary prevents navigation to an external site from contaminating application coverage.

For the modern Dimeshift application metric, direct application-owned JavaScript served under:

```text
/scripts/
```

is measured because no source map is available.

## Coverage Output

Runtime coverage artifacts are written under:

```text
coverage-results/
```

For example:

```text
coverage-results/petclinic/selenium4-three-metrics/
```

The final postprocessed console summary is:

```text
THREE_COVERAGE_RESULTS
DANTE-compatible  : ...
Modern application: ...
Raw bundle        : ...
```

Execution logs are written under:

```text
logs/
```

`coverage-results/` and `logs/` are intended as local generated artifacts and are ignored by Git in the current repository.

Selected final experiment summaries can instead be retained under:

```text
experiments/
```

## Baseline Utilities

The repository also contains experimental baseline runners used for comparison with the Recursive LLM pipeline:

```text
run-global-baseline.sh
run-startup-baseline.sh
```

Associated helper files/results are stored under:

```text
scripts/startup-baseline/
global-baseline-results/
```

These baselines are separate from the default `run-testception.sh --full` Recursive LLM workflow.

## Useful Commands

Full run with compact progress and reduced runtime overhead:

```bash
./run-testception.sh splittypie --full --fast --progress
```

Watch browser interactions:

```bash
./run-testception.sh retroboard \
  --full \
  --fast \
  --progress \
  --headed
```

Rerun only the existing Selenium 4 coverage suite:

```bash
./run-testception.sh phoenix \
  --coverage \
  --fast \
  --progress
```

Increase the Recursive Worker token budget:

```bash
TESTCEPTION_WORKER_MAX_TOKENS=3000 \
  ./run-testception.sh petclinic \
  --full \
  --fast \
  --progress
```

Run more state-local exploration iterations:

```bash
TESTCEPTION_STATE_ITERATIONS=25 \
  ./run-testception.sh petclinic \
  --full \
  --fast \
  --progress
```

## Repository Hygiene

The repository intentionally excludes local/generated development artifacts such as:

```text
*.bak
*.bak-*
*.before-*
__pycache__/
logs/
coverage-results/
```

Large raw coverage traces should remain local unless they are explicitly selected as research artifacts.

## Attribution

Testception builds on the DANTE crawling infrastructure and reuses parts of the original DANTE project.

Original DANTE submission material:

<https://github.com/matteobiagiola/TREL26-Submission-Material-Testception>
