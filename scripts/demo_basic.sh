#!/usr/bin/env bash
# Проверяет оба параметра по отдельности и вместе, в обеих формах записи.
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

section "no parameters"
emu

section "--vfs only"
emu --vfs "$VFS"

section "--script only"
emu --script "$ROOT/examples/startup.emu"

section "--vfs and --script"
emu --vfs "$VFS" --script "$ROOT/examples/startup.emu"

section "--option=value form"
emu --vfs="$VFS" --script="$ROOT/examples/startup.emu"
