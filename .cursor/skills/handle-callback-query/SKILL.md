---
name: handle-callback-query
description: Handles inline keyboard button presses and provides user feedback via answerCallbackQuery. Use when processing callback queries, answering button clicks, or updating messages after inline button interaction.
---

# Handle Callback Query

## Quick Start

Register a handler and **always** answer the callback query to stop the loading state:

```kotlin
bot.setFunctionality {
    onCallbackQuery {
        val cq = update.callbackQuery
        answerCallbackQuery(cq.id).options { text = "Done!" }.send(user, bot)
        when (cq.data) {
            "yes" -> message("You said yes!").send(user, bot)
            "no" -> message("You said no.").send(user, bot)
        }
    }
}
```

## Handler Registration

- **DSL**: `onCallbackQuery { }` — typed `ActivityCtx<CallbackQueryUpdate>`
- **Annotation**: `@CommandHandler.CallbackQuery(["prefix"])` with optional `autoAnswer = true`

## Accessing Callback Data

- `update.callbackQuery` — the CallbackQuery
- `callbackQuery.id` — required for answerCallbackQuery
- `callbackQuery.data` — button data (1-64 bytes), may be null
- `callbackQuery.from` — user who pressed
- `callbackQuery.message` — message with the button (if any)

## Answering the Query

**Always call** `answerCallbackQuery` to remove the loading state:

```kotlin
answerCallbackQuery(callbackQuery.id).send(user, bot)
```

With options:

```kotlin
answerCallbackQuery(callbackQuery.id).options {
    text = "Saved!"           // Notification text (0-200 chars)
    showAlert = true         // Alert instead of top notification
    url = "https://..."      // URL to open (e.g. t.me/bot?start=xxx)
    cacheTime = 60           // Cache result (seconds)
}.send(user, bot)
```

## Parsing Callback Data

Use prefixes for routing: `"action:id"`, `"vote:123"`:

```kotlin
cq.data?.split(":", limit = 2)?.let { (action, id) ->
    when (action) {
        "vote" -> handleVote(id.toLong())
        "page" -> showPage(id.toInt())
    }
}
```

## @ParamMapping

Maps handler parameters to keys in the parsed parameters map. Use with `@CommandHandler.CallbackQuery` when the ArgumentParser extracts named values from callback data (e.g. `page=1`, `id=123`):

```kotlin
@CommandHandler.CallbackQuery(["vote"], autoAnswer = true)
suspend fun handleVote(
    @ParamMapping("id") itemId: Long,
    user: User,
    bot: TelegramBot
) {
    message("Voted for $itemId").send(user, bot)
}
```

- `@ParamMapping("name")` — parameter is resolved from `parameters["name"]` (parsed by ArgumentParser)
- Supported types: String, Int, Long, Short, Float, Double
- Use when callback data exceeds simple routing and you need typed parameters

## Updating the Message

After handling, optionally edit the message or its markup:

- `editMessageText` — change text
- `editMessageReplyMarkup` — change/remove buttons

## Reference

- [AnswerCallbackQuery.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api/answer/AnswerCallbackQuery.kt)
- [CallbackQuery.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/common/CallbackQuery.kt)
- [ParamMapping.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/annotations/ParamMapping.kt)
- [FunctionalDSLUtils.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/common/FunctionalDSLUtils.kt)
