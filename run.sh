#!/usr/bin/env bash
# Builds the emulator when sources changed and starts it with the given args.
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
