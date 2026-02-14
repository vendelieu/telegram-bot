# AGENTS.md

Project context and conventions for AI coding assistants working on the telegram-bot library.

## Dev environment

- **Build**: `./gradlew prepareRelease` before committing (format, api validation, kdocs, api violations check)
- **Kotlin**: Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Target branch**: **dev** for PRs

## Testing

- Add tests for new code
- Run `./gradlew prepareRelease` to validate
- Run `./gradlew apiDump` to generate api dump

## Project structure

- `telegram-bot/` — main library
- `ktnip/` — KSP processor for annotations
- `api-sentinel/` — API validation against Telegram spec

## Key conventions

- Use `@TgAPI` on Action classes and top-level API functions for validation
- Required params must be non-null; optional params must be nullable
- Method names must match `@TgAPI.Name` annotation
- For Activity, WizardActivity, functional DSL vs annotation approach, and ProcessingPipeline, see [.cursor/rules/telegram-bot-handling.mdc](.cursor/rules/telegram-bot-handling.mdc)

## Cursor-specific resources

- **Rules**: `.cursor/rules/` — API, handling, types, tests, KSP processor
- **Skills**: `.cursor/skills/` — organized by audience:
  - `library-dev/` — for contributors extending the library (API actions, update fields, KSP collectors)
  - `bot-user/` — for developers building bots (keyboards, handlers, wizards, callback queries)
- See [.cursor/skills/README.md](.cursor/skills/README.md) for the full skill index
