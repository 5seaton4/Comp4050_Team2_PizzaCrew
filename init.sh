#!/bin/bash

#NOTE(Jack): Exit on pipeline errors.
set -e

echo "Copying Pre Commit Hooks and Setting Permissions"

#NOTE(Jack): Using copy instead of symlinks to be compatible between OS.
if test -f "git_hooks/pre-commit";
then
    cp git_hooks/pre-commit .git/hooks/pre-commit
    #NOTE(Jack): Make the files executable.
    chmod +x .git/hooks/pre-commit
fi

echo "Copying Prepare Commit Message Hooks and Setting Permissions"

if test -f "git_hooks/prepare-commit-msg";
then
    cp git_hooks/prepare-commit-msg .git/hooks/prepare-commit-msg
    chmod +x .git/hooks/prepare-commit-msg
fi
