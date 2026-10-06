#!/usr/bin/env bash
# Checks quoting in the startup script and in interactive input.
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

section "--vfs and --script with quotes"
emu --vfs "$VFS" --script "$ROOT/examples/quotes.emu"

section "--script only, other VFS name"
emu --vfs "$ROOT/examples/my project.v1.json" --script "$ROOT/examples/quotes.emu"

section "interactive input after the script"
printf 'ls "a b"\ncd '"'"'c d'"'"'\nexit\n' \
    | "$EMU" --vfs "$VFS" --script "$ROOT/examples/no_exit.emu"
printf '[exit code: %d]\n' "$?"
