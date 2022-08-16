#!/bin/bash

#NOTE(Jack): Exit on pipeline errors.
set -e

#NOTE(Jack): Using copy instead of symlinks to be compatible between OS.
cp git_hooks/pre-commit .git/hooks/pre-commit
cp git_hooks/prepare-commit-msg .git/hooks/prepare-commit-msg

#NOTE(Jack): Make the files executable.
chmod +x .git/hooks/pre-commit
chmod +x .git/hooks/prepare-commit-msg
