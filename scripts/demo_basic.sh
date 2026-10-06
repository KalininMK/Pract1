#!/usr/bin/env bash
# Checks both parameters, alone and together, in both option forms.
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
