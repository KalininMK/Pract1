#!/usr/bin/env bash
# Shared helpers for the demo scripts.

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
EMU="$ROOT/run.sh"
VFS="$ROOT/examples/demo.json"

# Prints a section header.
section() {
    printf '\n=== %s ===\n' "$1"
}

# Runs the emulator with empty stdin and reports its exit code.
emu() {
    printf '$ run.sh %s\n' "$*"
    "$EMU" "$@" < /dev/null
    printf '[exit code: %d]\n' "$?"
}
