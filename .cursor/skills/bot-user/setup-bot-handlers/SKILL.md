---
name: setup-bot-handlers
description: [Bot developer] Sets up bot handlers using DSL or annotations. Use when configuring command handlers, input handlers, common handlers, update handlers, or fallback handlers for the telegram-bot library.
---

# Setup Bot Handlers

## Two Approaches

- **DSL**: `bot.setFunctionality { ... }` — define handlers inline
- **Annotations**: `@CommandHandler`, `@InputHandler`, etc. — KSP generates activities; use with `bot.handleUpdates()` (loads handlers via META-INF)

## DSL Handlers

Inside `bot.setFunctionality { }`:

| Handler | Purpose |
|---------|---------|
| `onCommand("/start") { }` | Commands (e.g. /start) |
| `onInput("id") { }` | Single-step input; route via `bot.inputListener[user] = "id"` |
| `inputChain("id") { }.andThen { }` | Multi-step input chain |
| `common("text") { }` or `common(Regex("...")) { }` | Text/regex match (lower priority than commands) |
| `onUpdate(UpdateType.X) { }` | All updates of given type |
| `whenNotHandled { }` | Fallback for unprocessed updates |

## Typed Update Handlers

Use typed wrappers for `ActivityCtx<XxxUpdate>`:

- `onMessage { }` — `MessageUpdate`
- `onCallbackQuery { }` — `CallbackQueryUpdate`
- `onInlineQuery { }` — `InlineQueryUpdate`
- `onEditedMessage { }`, `onChannelPost { }`, `onChatMember { }`, etc.

## Input Listener

Route the next user message to an input handler or chain:

```kotlin
bot.inputListener[user] = "conversation"
// or
bot.inputListener.set(user) { "conversation-2step" }
```

## Annotation Handlers

| Annotation | Purpose |
|------------|---------|
| `@CommandHandler(["/start"])` | Command handler |
| `@CommandHandler.CallbackQuery(["/data"])` | Callback query with optional `autoAnswer` |
| `@InputHandler(["id"])` | Input handler |
| `@CommonHandler.Text(["value"])` or `@CommonHandler.Regex("...")` | Text/regex match |
| `@UpdateHandler([UpdateType.MESSAGE, ...])` | Update type handler |
| `@UnprocessedHandler` | Fallback |

## Guards

Add pre-processing checks:

- DSL: `guard = UserPresentGuard::class` (or `DefaultGuard::class`)
- Annotation: `@Guard(UserPresentGuard::class)` or `@Guard(guard = UserPresentGuard::class)`

Supported by: CommandHandler, CommandHandler.CallbackQuery, InputHandler.

## Rate Limits

- DSL: `rateLimits = RateLimits.NOT_LIMITED` or custom
- Annotation: `@RateLimits(period = 60_000, rate = 5)` — 5 requests per 60 seconds
- Default: no limit

Supported by: CommandHandler, CallbackQuery, InputHandler, CommonHandler.

## ArgParser

Custom argument parsing for commands and callback data:

- Annotation: `@ArgParser(argParser = CustomArgParser::class)`
- Supported by: CommandHandler, CallbackQuery, CommonHandler
- Default: `DefaultArgParser`

## Annotation Reference

| Annotation | Parameters | Default |
|------------|------------|---------|
| `@CommandHandler` | `value` (commands), `scope` (UpdateType[]) | scope: MESSAGE |
| `@CommandHandler.CallbackQuery` | `value` (callback data prefixes), `autoAnswer` (bool) | autoAnswer: false |
| `@InputHandler` | `value` (input identifiers) | — |
| `@CommonHandler.Text` | `value`, `filters`, `priority`, `scope` | filters: [], priority: 0, scope: MESSAGE |
| `@CommonHandler.Regex` | `value` (pattern), `options`, `filters`, `priority`, `scope` | options: [], filters: [], priority: 0, scope: MESSAGE |
| `@UpdateHandler` | `type` (UpdateType[]) | — |
| `@UnprocessedHandler` | — | — |
| `@Guard` | `guard` (KClass) | DefaultGuard |
| `@ArgParser` | `argParser` (KClass) | DefaultArgParser |
| `@RateLimits` | `period` (ms), `rate` (requests) | 0, 0 |

## Reference

- [CommandHandler.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/annotations/CommandHandler.kt)
- [CommonHandler.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/annotations/CommonHandler.kt)
- [FunctionalHandlingDsl.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/core/FunctionalHandlingDsl.kt)
- [FunctionalDSLUtils.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/common/FunctionalDSLUtils.kt)
- [telegram-bot-handling.mdc](.cursor/rules/telegram-bot-handling.mdc)
