#!/bin/bash

# Default changelog file path
DEFAULT_CHANGELOG_FILE="CHANGELOG.md"

# Function to extract changelog section based on the version number
extract_changelog_section() {
    local version="$1"
    local file="$2"

    # Escape the period for grep to interpret it as a literal period
    local version_escaped=$(echo "$version" | sed 's/\./\\./g')

    # Find the section for the given version, including the heading
    awk -v version="$version_escaped" '
    BEGIN { capture=0 }
    # Match the beginning of the section with the specified version, including the first line (heading)
    $0 ~ "^#+[[:space:]]"version {
        capture=1
    }
    # Stop capturing when the next version section is encountered
    capture && $0 ~ "^#+[[:space:]][0-9]+\\.[0-9]+\\.[0-9]+" && !($0 ~ "^#+[[:space:]]"version) {
        exit
    }
    # Print the lines when capture is enabled
    capture { print }
    ' "$file"
}

# Ensure a version number is provided
if [ -z "$1" ]; then
    echo "Usage: $0 <version> [changelog_file]"
    exit 1
fi

# Use the provided changelog file or the default if not provided
changelog_file="${2:-$DEFAULT_CHANGELOG_FILE}"

# Call the function with provided arguments
extract_changelog_section "$1" "$changelog_file"
