#!/usr/bin/env bash
# Собирает эмулятор при изменении исходников и запускает его с переданными аргументами.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVAC="${JAVAC:-javac}"
BUILD="$ROOT/build/main"
STAMP="$BUILD/.stamp"

needs_build() {
    [ ! -f "$STAMP" ] || [ -n "$(find "$ROOT/src" -name '*.java' -newer "$STAMP")" ]
}

if needs_build; then
    mkdir -p "$BUILD"
    # shellcheck disable=SC2046
    $JAVAC -d "$BUILD" $(find "$ROOT/src" -name '*.java')
    touch "$STAMP"
fi

exec java -cp "$BUILD" emulator.Main "$@"
