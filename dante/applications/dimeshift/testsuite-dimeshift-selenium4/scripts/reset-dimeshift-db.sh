#!/usr/bin/env bash
set -Eeuo pipefail

SUITE_DIR="$(
    cd "$(dirname "${BASH_SOURCE[0]}")/.." &&
    pwd
)"

APP_DIR="$(
    cd "$SUITE_DIR/.." &&
    pwd
)"

ROOT_DIR="$(
    cd "$APP_DIR/../../.." &&
    pwd
)"

SQL_FILE="$APP_DIR/testsuite-dimeshift/src/main/resources/dimeshift.sql"
PROPERTIES_FILE="$ROOT_DIR/dante/src/main/resources/app.properties"

[[ -f "$SQL_FILE" ]] || {
    echo "DIMESHIFT_DB_RESET_ERROR: SQL dump not found: $SQL_FILE" >&2
    exit 2
}

DB_PORT="${TESTCEPTION_DB_PORT:-}"

if [[ -z "$DB_PORT" && -f "$PROPERTIES_FILE" ]]; then
    DB_PORT="$(
        grep -E '^db_port=' "$PROPERTIES_FILE" \
        | tail -n 1 \
        | cut -d= -f2- \
        | tr -d '[:space:]' \
        || true
    )"
fi

DB_PORT="${DB_PORT:-3306}"
DB_NAME="${TESTCEPTION_DB_NAME:-walletjs}"
DB_USER="${TESTCEPTION_DB_USER:-root}"
DB_PASSWORD="${TESTCEPTION_DB_PASSWORD:-root}"

run_with_host_mysql() {
    command -v mysql >/dev/null 2>&1 || return 1

    MYSQL_PWD="$DB_PASSWORD" \
        mysql \
        --protocol=TCP \
        -h 127.0.0.1 \
        -P "$DB_PORT" \
        -u "$DB_USER" \
        "$DB_NAME" \
        < "$SQL_FILE"
}

find_mysql_container() {
    local id name ports

    while IFS='|' read -r id name ports; do
        [[ -n "$id" ]] || continue

        if [[ "$ports" == *"${DB_PORT}->3306/tcp"* ]] || \
           [[ "$name" == *dimeshift* ]]; then
            if docker exec "$id" sh -lc \
                'command -v mysql >/dev/null 2>&1' \
                >/dev/null 2>&1; then
                echo "$id"
                return 0
            fi
        fi
    done < <(
        docker ps --format '{{.ID}}|{{.Names}}|{{.Ports}}'
    )

    return 1
}

run_with_container_mysql() {
    command -v docker >/dev/null 2>&1 || return 1

    local container
    container="$(find_mysql_container)" || return 1

    docker exec -i \
        -e MYSQL_PWD="$DB_PASSWORD" \
        "$container" \
        mysql \
        -u "$DB_USER" \
        "$DB_NAME" \
        < "$SQL_FILE"
}

echo "DIMESHIFT_DB_RESET_START | db=$DB_NAME | port=$DB_PORT"

for attempt in 1 2 3 4 5; do
    if run_with_host_mysql; then
        echo "DIMESHIFT_DB_RESET_DONE | transport=host-mysql | attempt=$attempt"
        exit 0
    fi

    if run_with_container_mysql; then
        echo "DIMESHIFT_DB_RESET_DONE | transport=docker-mysql | attempt=$attempt"
        exit 0
    fi

    echo "DIMESHIFT_DB_RESET_RETRY | attempt=$attempt" >&2
    sleep 1
done

echo "DIMESHIFT_DB_RESET_ERROR: could not restore walletjs from $SQL_FILE" >&2
exit 3
