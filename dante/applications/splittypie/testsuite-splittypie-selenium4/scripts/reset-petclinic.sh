#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(cd "$SCRIPT_DIR/../../../../.." && pwd)"
NEW="${NEW:-$ROOT}"

CLIENT_NAME="${PETCLINIC_CLIENT_CONTAINER:-petclinic-selenium4-client}"
SERVER_NAME="${PETCLINIC_SERVER_CONTAINER:-petclinic-selenium4-server}"

docker rm -fv "$CLIENT_NAME" "$SERVER_NAME" 2>/dev/null || true

cd "$NEW/dante/docker/petclinic"
./run-docker.sh -p yes -n petclinic-selenium4
