# Testception

This repository contains the current Testception prototype and the subject applications used during development and evaluation. The `selenium4-rlm` branch implements a multi-stage pipeline that keeps the legacy DANTE/Crawljax crawling environment separate from the Recursive LLM and Selenium 4 execution/coverage layers.

Testception has been developed and tested on **Ubuntu 24.04 LTS**.


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

Docker is used by the current Petclinic reset/start scripts.

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

The Recursive LLM code is under:

```text
Testception/rlm_project/
```

The main components are:

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

Read the OpenRouter API key without echoing it:

```bash
export OPENROUTER_API_KEY="your_key"
```

API keys and model configuration are intentionally **not stored in the repository or in a committed `.env` file**.

## Running Testception

The main entry point is:

```bash
./run-testception.sh
```

### Full Pipeline

```bash
./run-testception.sh petclinic --full
```

`--full` performs the complete workflow:

1. switches to Java 8;
2. performs a fresh Crawljax crawl through the existing legacy crawler;
3. loads the new `result.json` and saved DOM states;
4. runs Recursive LLM scenario generation;
5. generates the DANTE-compatible Java test suite;
6. removes scenarios that are exact prefixes of longer scenarios;
7. converts the generated suite into the Selenium 4 project;
8. verifies that legacy and Selenium 4 test counts match;
9. switches to Java 17;
10. executes the Selenium 4 tests; and
11. calculates the three supported JavaScript coverage metrics.

The legacy crawler remains unchanged and is invoked through:

```text
dante/run-crawling.sh
```

### Coverage-Only

```bash
./run-testception.sh petclinic --coverage
```

`--coverage` skips:

```text
Crawljax
Recursive LLM generation
Java scenario generation
```

and executes the currently available Selenium 4 test suite directly.

This mode is useful for rerunning an already generated suite, debugging Selenium/coverage code, or evaluating coverage without another crawl and LLM run.

`--coverage` does **not** require an OpenRouter API key or LLM model configuration.

### Crawl Duration

Override the default crawl duration with:

```bash
TESTCEPTION_CRAWL_MINUTES=15 \
  ./run-testception.sh petclinic --full
```

### Headed / Headless Execution

```bash
./run-testception.sh petclinic --full --headed
```

```bash
./run-testception.sh petclinic --coverage --headless
```

Headed execution is useful when debugging generated browser interactions.

## Crawljax Artifacts

A fresh full run creates crawl artifacts under:

```text
dante/applications/<app>/localhost/crawl-with-inputs/
```

Important artifacts include:

```text
result.json
doms/
```

`result.json` contains recorded crawl transitions. Saved DOM states are parsed by the Recursive LLM layer.

Testception uses two exploration modes:

```text
GRAPH_FIRST
DOM_FALLBACK
```

`GRAPH_FIRST` prioritizes currently unconsumed transitions already recorded by Crawljax.

`DOM_FALLBACK` allows the LLM to reason about additional actionable DOM elements when no useful recorded transition remains for the current state.

## Selenium 4 Coverage

The Selenium 4 execution layer collects JavaScript coverage from the same ChromeDriver used to perform test actions.

Raw CDP commands are issued through Selenium 4 using:

```text
HasCdp.executeCdpCommand(...)
```

The Selenium 4 Testception path does not use the old cdp4j-based DANTE session.

Coverage is executed in two passes because DANTE-compatible function coverage and modern detailed V8 coverage require different profiler configurations.

### 1. DANTE-Compatible Coverage

This metric reproduces the historical DANTE coverage semantics as closely as possible while collecting data through Selenium 4/CDP.

For Petclinic it focuses on:

```text
main.js
```

This metric is intended primarily for comparison with DANTE.

### 2. Modern Application Coverage

Source-map-aware coverage restricted to application-owned original source lines.

This provides a modern application-level view that excludes unrelated bundled/vendor code where possible.

### 3. Raw Bundle Coverage

Raw generated JavaScript byte coverage over accepted JavaScript bundles.

This is mainly a low-level diagnostic measurement.

## Coverage Output

Coverage results are written under:

```text
coverage-results/
```

For Petclinic:

```text
coverage-results/petclinic/selenium4-three-metrics/
```

The final console summary contains:

```text
THREE_COVERAGE_RESULTS
DANTE-compatible : ...
Modern application: ...
Raw bundle       : ...
```

Execution logs are stored under:

```text
logs/
```

## Attribution

Testception builds on the DANTE crawling infrastructure and reuses parts of the original DANTE project.

Original DANTE submission material:

<https://github.com/matteobiagiola/TREL26-Submission-Material-Testception>


