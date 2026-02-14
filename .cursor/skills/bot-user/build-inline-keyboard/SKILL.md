---
name: build-inline-keyboard
description: [Bot developer] Builds inline keyboards for messages using the telegram-bot DSL. Use when adding buttons under messages, creating callback buttons, URL buttons, or inline keyboard markup for user interaction.
---

# Build Inline Keyboard

## Quick Start

Use `inlineKeyboardMarkup { }` or `inlineKeyboardMarkup(block)` to create inline keyboards. Attach via `markup()` or `inlineKeyboardMarkup { }` on send actions.

```kotlin
message("Choose an option:")
    .inlineKeyboardMarkup {
        "Option A" callback "opt_a"
        "Option B" callback "opt_b"
        newLine()
        "Open link" url "https://example.com"
    }
    .send(user, bot)
```

## Button Types

| Type | Syntax | Use case |
|------|--------|----------|
| Callback | `"Label" callback "data"` or `callbackData("Label") { "data" }` | Button press sends callback to bot (1-64 bytes) |
| URL | `"Label" url "https://..."` | Opens URL when pressed |
| Web App | `"Label" webAppInfo "https://..."` | Opens Web App |
| Switch inline | `"Label" switchInlineQuery "query"` | Opens inline mode in another chat |
| Switch inline (current) | `"Label" switchInlineQueryCurrentChat "query"` | Inserts bot + query in current chat |
| Pay | `"Pay" pay()` | Payment button |
| Copy text | `"Label" copyText "text"` | Copies text to clipboard |

## Rows

- Each `url()`, `callbackData()`, etc. adds a button to the current row
- Use `newLine()` or `br()` to start a new row

```kotlin
inlineKeyboardMarkup {
    "Yes" callback "yes"
    "No" callback "no"
    newLine()
    "Cancel" callback "cancel"
}
```

## Attaching to Messages

Actions with `MarkupFeature` (e.g. `message`, `sendPhoto`, `editMessageText`) support:

- `inlineKeyboardMarkup { ... }` — builder block
- `markup(inlineKeyboardMarkup { ... })` — explicit markup

## Callback Data Limits

- 1-64 bytes for `callbackData`
- Use prefixes for routing: `"action:id"`, `"vote:123"`

## Reference

- [InlineKeyboardMarkupBuilder.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/builders/InlineKeyboardMarkupBuilder.kt)
- [MarkupFeature.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/interfaces/features/MarkupFeature.kt)
