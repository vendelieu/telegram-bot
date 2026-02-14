---
name: build-reply-keyboard
description: [Bot developer] Builds reply keyboards for messages using the telegram-bot DSL. Use when adding persistent or one-time keyboards, request contact/location buttons, or Web App buttons for user input.
---

# Build Reply Keyboard

## Quick Start

Use `replyKeyboardMarkup { }` or `replyKeyboardMarkup(block)`. Attach via `markup()` or `replyKeyboardMarkup { }` on send actions.

```kotlin
message("Choose an option:")
    .replyKeyboardMarkup {
        +"Option A"
        +"Option B"
        newLine()
        "Share contact" requestContact true
        options { resizeKeyboard = true; oneTimeKeyboard = true }
    }
    .send(user, bot)
```

## Simple Buttons

Use unary plus to add plain text buttons:

```kotlin
+"Button 1"
+"Button 2"
newLine()
+"Next row"
```

## Special Buttons

| Type | Syntax | Availability |
|------|--------|--------------|
| Request contact | `"Share contact" requestContact true` | Private chats only |
| Request location | `"Share location" requestLocation true` | Private chats only |
| Request user | `"Select user" requestUser KeyboardButtonRequestUsers(...)` | Private chats only |
| Request chat | `"Select chat" requestChat KeyboardButtonRequestChat(...)` | Private chats only |
| Request poll | `"Create poll" requestPoll KeyboardButtonPollType(...)` | Private chats only |
| Web App | `"Open app" webApp "https://..."` | Private chats only |

## Options

Configure via `options { }`:

- `resizeKeyboard = true` — keyboard resizes to fit screen
- `oneTimeKeyboard = true` — keyboard hides after one use
- `inputFieldPlaceholder = "Type here..."` — placeholder in input field (1-64 chars)
- `selective = true` — show only to @mentioned users or reply target
- `isPersistent = true` — keyboard stays visible (for oneTimeKeyboard)

## Rows

Use `newLine()` or `br()` to start a new row, same as inline keyboards.

## Removing the Keyboard

When done, send a message with `replyKeyboardRemove()`:

```kotlin
message("Keyboard removed")
    .replyKeyboardRemove()
    .send(user, bot)
```

Or `replyKeyboardRemove(selective = true)` for selective removal.

## Reference

- [ReplyKeyboardMarkupBuilder.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/builders/ReplyKeyboardMarkupBuilder.kt)
- [ReplyKeyboardMarkupOptions.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/options/ReplyKeyboardMarkupOptions.kt)
- [MarkupFeature.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/interfaces/features/MarkupFeature.kt)
