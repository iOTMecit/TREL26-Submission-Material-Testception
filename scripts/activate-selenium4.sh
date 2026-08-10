#!/usr/bin/env bash

set -e

export TESTCEPTION_ROOT="$HOME/workspace/TREL26-Submission-Material-Testception-selenium4"

export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
export PATH="$JAVA_HOME/bin:$PATH"
export TESTCEPTION_MAVEN_REPO="$TESTCEPTION_ROOT/.m2-selenium4/repository"

export TESTCEPTION_COVERAGE_DIR="$TESTCEPTION_ROOT/coverage-results"

mkdir -p \
  "$TESTCEPTION_ROOT/.m2-selenium4/repository" \
  "$TESTCEPTION_COVERAGE_DIR"

# DANTE scriptleri mvn komutunu kendileri çağırdığı için
# Maven repository ayarını environment üzerinden veriyoruz.
export MAVEN_OPTS="\
-Dmaven.repo.local=$TESTCEPTION_ROOT/.m2-selenium4/repository \
${MAVEN_OPTS:-}"

VENV="$TESTCEPTION_ROOT/Testception/rlm_project/.venv-selenium4"

if [ -f "$VENV/bin/activate" ]; then
    source "$VENV/bin/activate"
fi

echo
echo "TESTCEPTION_ROOT=$TESTCEPTION_ROOT"
echo "JAVA_HOME=$JAVA_HOME"
echo "Maven repository=$TESTCEPTION_ROOT/.m2-selenium4/repository"
echo "Coverage directory=$TESTCEPTION_COVERAGE_DIR"
echo

java -version
mvn -version
