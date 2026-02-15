#!/bin/sh
# Installs git hooks. Run from project root: ./scripts/install-hooks.sh

set -e
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
HOOKS_DIR="$ROOT/.git/hooks"

mkdir -p "$HOOKS_DIR"
cp "$ROOT/scripts/pre-push" "$HOOKS_DIR/pre-push"
chmod +x "$HOOKS_DIR/pre-push"
echo "Installed pre-push hook. prepareRelease will run before each push."
