---
name: add-telegram-api-tests
description: Writes tests for Telegram Bot API actions. Use when adding tests for an API action, testing a new Telegram method, or when the user asks to add tests for the telegram-bot library.
---

# Add Telegram API Tests

## Workflow

### 1. Test Class Setup

- Extend **BotTestContext** (provides `bot`, `TG_ID`, `CHAT_ID`, `CHANNEL_ID`)
- Use Kotest **AnnotationSpec** with `@Test suspend fun`
- Test names: backtick string e.g. `` `method name method test` ``

```kotlin
class MyApiTest : BotTestContext() {
    @Test
    suspend fun `my method test`() {
        // ...
    }
}
```

### 2. API Call Helpers

- **Action** (chat-based): `action.sendReq(to = TG_ID, via = bot)` or `action.sendReq(CHAT_ID)`
- **SimpleAction**: `action.sendReq(via = bot)` or `action.sendReq()`
- **MediaAction**: same as Action
- Defaults: `sendReq()` uses `TG_ID` and `bot` when omitted

### 3. Assertions

- Success: `.shouldSuccess()` - asserts ok, isSuccess, getOrNull not null
- Optional result: `.getOrNull()` when success not guaranteed
- Failure: `.shouldFailure()` - returns `Response.Failure`
- Error text: `response.shouldFailure() shouldContainInDescription "ERROR_TEXT"`

### 4. Rate Limits (429)

For methods that may hit rate limits (e.g. setMyName):

```kotlin
val result = setMyName("test").sendReq()
result.onFailure { if (it.errorCode == 429) return }
result.shouldSuccess()
```

### 5. Test Placement

- Path: `telegram-bot/src/jvmTest/kotlin/eu/vendeli/api/`
- Mirror API package: `botactions/`, `media/`, `message/`, `chat/`, etc.
- Add to existing test class if one exists for that API area

### 6. Environment

Tests load from `.env` or system env. Required for most tests:
- `TELEGRAM_ID` - your Telegram user ID
- `BOT_TOKEN` - bot token from BotFather

Optional:
- `CHAT_ID` - for chat/group tests
- `CHANNEL_ID` - for channel tests
- `BOT_TOKEN_2` - rate-limit testing

## Reference Examples

- **Bot actions**: [SetMyActionsTest.kt](telegram-bot/src/jvmTest/kotlin/eu/vendeli/api/botactions/SetMyActionsTest.kt)
- **User/chat**: [UserTest.kt](telegram-bot/src/jvmTest/kotlin/eu/vendeli/api/UserTest.kt)
- **Message actions**: [MessageActionsTest.kt](telegram-bot/src/jvmTest/kotlin/eu/vendeli/api/MessageActionsTest.kt)
- **Failure assertion**: [MessageActionsTest.kt](telegram-bot/src/jvmTest/kotlin/eu/vendeli/api/MessageActionsTest.kt) - `shouldFailure() shouldContainInDescription`
