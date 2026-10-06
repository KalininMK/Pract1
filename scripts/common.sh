#!/usr/bin/env bash
# Общие функции для демонстрационных скриптов.

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
EMU="$ROOT/run.sh"
VFS="$ROOT/examples/demo.json"

# Печатает заголовок раздела.
section() {
    printf '\n=== %s ===\n' "$1"
}

# Запускает эмулятор с пустым stdin и выводит его код возврата.
emu() {
    printf '$ run.sh %s\n' "$*"
    "$EMU" "$@" < /dev/null
    printf '[exit code: %d]\n' "$?"
}
