#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export TESTCEPTION_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "TESTCEPTION_ROOT=$TESTCEPTION_ROOT"
