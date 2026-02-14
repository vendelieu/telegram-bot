---
name: add-telegram-api-action
description: [Library contributor] Adds a new Telegram Bot API method (action class and top-level functions). Use when implementing a new Telegram API endpoint, adding support for a specific method (e.g. sendVideoNote, getChatMemberCount), or creating a new action for the telegram-bot library.
---

# Add Telegram API Action

## Workflow

### 1. Determine Action Type

- **Requires chat_id?** -> `Action` (or `MediaAction` for media)
- **No chat context?** -> `SimpleAction` (e.g. getMe, getUpdates, setWebhook)

### 2. Check Telegram API Spec

Consult [core.telegram.org/bots/api](https://core.telegram.org/bots/api) for:
- Method name (snake_case)
- Required vs optional parameters
- Return type

### 3. Create Options Class (if optional params)

- Place in `telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/options/`
- `data class` implementing `Options` + mixins (`OptionsCommon`, `OptionsParseMode`, `MediaSpoiler`, `ForumProps`, etc.)
- All optional params: `var prop: Type? = null`
- Add `@Serializable`
- **Mixin interfaces**: Put optional params into separate mixin interfaces in IOptions.kt when they can be reused by other Options classes. Add a new interface (e.g. `interface MyProp : Options { var myParam: Type? }`) only when the param is shared; single-use params stay in the data class.

### 4. Create Action Class

Place in appropriate package under `telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api/`:
- `media/` - photo, video, document, etc.
- `message/` - sendMessage, editMessage, etc.
- `answer/` - answerCallbackQuery, answerInlineQuery
- `botactions/` - getMe, setWebhook, etc.
- `chat/` - getChat, banChatMember, etc.

**Required:**
- `@TgAPI` on class
- `@TgAPI.Name("methodName")` on `method` when Kotlin name differs from API
- `override val method`, `returnType`, `options` (if OptionsFeature)

**Features** (add as needed):
- `OptionsFeature` - optional params via `options { }`
- `MarkupFeature` - reply_markup
- `CaptionFeature` - caption, caption_entities (media)
- `EntitiesFeature` - entities (text)
- `BusinessActionExt` - chat-based actions
- `InlineActionExt` - inline message actions

**Parameter handling:**
- Simple (String, Long, Int, Boolean): `parameters["key"] = value.toJsonElement()`
- Complex objects: `value.encodeWith(MyType.serializer())`
- Lists: `list.encodeWith(ListSerializer(ItemType.serializer()))`
- Polymorphic ID (chat/user: Long or String): `value.encodeWith(DynamicLookupSerializer)`
- Enums: `value.encodeWith(EnumType.serializer())`
- Required media: `handleImplicitFile(file, "paramName")` in `init`
- Optional media in Options (thumbnail): `handleImplicitFile(options::thumbnail)` in `override val beforeReq`
- Complex types with nested ImplicitFile (InputProfilePhoto, InputStoryContent): transform in `beforeReq`, then `encodeWith(Serializer)`

Use `encodeWith` for any `@Serializable` class, list, or polymorphic type; use `toJsonElement()` only for primitives.

### 5. Add Top-Level Functions

- `@TgAPI` on each
- `inline fun actionName(...) = ActionClass(...)`
- Overloads for `ImplicitFile`, `InputFile`, `ByteArray`, `() -> String` as in existing media actions
- Optional `sendActionName` alias

### 6. Validate

```bash
./gradlew formatKotlin
./gradlew prepareRelease
```

## Reference Examples

- **Media with options**: [Photo.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api/media/Photo.kt)
- **Simple action with options**: [AnswerCallbackQuery.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api/answer/AnswerCallbackQuery.kt)
- **Complex type with beforeReq**: [SetMyProfilePhoto.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api/botactions/SetMyProfilePhoto.kt)
- **Media with optional thumbnail**: [Document.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api/media/Document.kt) - uses `beforeReq` for `handleImplicitFile(options::thumbnail)`
