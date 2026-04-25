#!/bin/sh
# Installs git hooks by pointing core.hooksPath at scripts/.
# Run from project root: ./scripts/install-hooks.sh
#
# Uninstall: git config --unset core.hooksPath

set -eu
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [ ! -d .git ] && [ ! -f .git ]; then
    echo "error: not a git repository: $ROOT" >&2
    exit 1
fi

chmod +x scripts/pre-push

git config core.hooksPath scripts

# A pre-existing copy in .git/hooks would be shadowed by core.hooksPath, but
# remove it to avoid future confusion if someone unsets the config.
if [ -f .git/hooks/pre-push ]; then
    rm -f .git/hooks/pre-push
    echo "Removed stale .git/hooks/pre-push."
fi

echo "Installed hooks via core.hooksPath=scripts."
echo "prepareRelease will run before each push (bypass: SKIP_PRE_PUSH=1 or --no-verify)."
