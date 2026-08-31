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
