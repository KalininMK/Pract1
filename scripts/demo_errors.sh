#!/usr/bin/env bash
# Checks error handling for parameters and for startup script contents.
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

section "errors inside the script (valid --vfs and --script)"
emu --vfs "$VFS" --script "$ROOT/examples/errors.emu"

section "script file not found"
emu --vfs "$VFS" --script "$ROOT/examples/missing.emu"

section "unknown option"
emu --vfs "$VFS" --verbose

section "option without value"
emu --script

section "empty value"
emu --vfs= --script "$ROOT/examples/startup.emu"

section "duplicate option"
emu --vfs "$VFS" --vfs "$VFS" --script "$ROOT/examples/startup.emu"
