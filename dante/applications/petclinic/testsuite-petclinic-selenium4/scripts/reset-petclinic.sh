#!/usr/bin/env bash
set -euo pipefail

NEW="${NEW:-$HOME/workspace/TREL26-Submission-Material-Testception-selenium4}"
CLIENT_NAME="${PETCLINIC_CLIENT_CONTAINER:-petclinic-selenium4-client}"
SERVER_NAME="${PETCLINIC_SERVER_CONTAINER:-petclinic-selenium4-server}"

# The DANTE docker helper creates a clean empty-db pair with these names.
docker rm -fv "$CLIENT_NAME" "$SERVER_NAME" 2>/dev/null || true

cd "$NEW/dante/docker/petclinic"
./run-docker.sh -p yes -n petclinic-selenium4
