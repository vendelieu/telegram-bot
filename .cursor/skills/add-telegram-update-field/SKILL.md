---
name: add-telegram-update-field
description: Adds a new field to the Update type and wires it through ProcessedUpdate, UpdateType, ProcessUpdate, KSP (TypeConstants, TypeMapper), and FunctionalDSLUtils. Use when implementing support for a new Telegram update type (e.g. message_reaction, chat_boost), adding a new update handler, or extending the bot to handle additional update kinds.
---

# Add Telegram Update Field

## Workflow

### 1. Add Field to Update

- In [Update.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/common/Update.kt): add `val myField: MyType? = null` (camelCase, nullable)
- Add import for the payload type if needed

### 2. Add UpdateType Entry

- In [UpdateType.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/UpdateType.kt): add enum entry with `@SerialName("snake_case")` matching Telegram API (e.g. `MY_FIELD`)

### 3. Add ProcessedUpdate Subclass

- In [ProcessedUpdate.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/ProcessedUpdate.kt): add `data class MyFieldUpdate(...) : ProcessedUpdate(...)`
- Use `@Serializable(MyFieldUpdate.Companion::class)` and `internal companion object : UpdateSerializer<MyFieldUpdate>()`
- Implement `UserReference` when the update has a user; `ChatReference` when it has a chat; `TextReference` when it has text

### 4. Add processUpdate Clause

- In [ProcessUpdate.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/common/ProcessUpdate.kt): add `myField != null -> MyFieldUpdate(updateId, this, myField)`
- Add import for the new ProcessedUpdate subclass
- Place clause in a logical order (group with similar update types)

### 5. Update KSP Module

- In [TypeConstants.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/utils/TypeConstants.kt): add `val myFieldUpdateClass: TypeName = MyFieldUpdate::class.asTypeName()`
- In [TypeMapper.kt](ktnip/src/jvmMain/kotlin/eu/vendeli/ktnip/codegen/TypeMapper.kt): add `TypeConstants.myFieldUpdateClass to MyFieldUpdate::class` to `updateTypeMap`

### 6. Regenerate FunctionalDSLUtils

- Run `./gradlew prepareRelease` (or full build)
- api-sentinel KSP regenerates [FunctionalDSLUtils.kt](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/utils/common/FunctionalDSLUtils.kt) from `UpdateType.entries`; no manual edits needed

**Handling**: The new `onXxx` DSL function enables `bot.setFunctionality { onXxx { ... } }`. Both functional DSL and annotation approach (`@UpdateHandler`) use the same ActivityRegistry and ProcessingPipeline; see [telegram-bot-handling.mdc](.cursor/rules/telegram-bot-handling.mdc).

## Naming Conventions

- **Update field**: camelCase (e.g. `callbackQuery`, `messageReaction`)
- **UpdateType**: SCREAMING_SNAKE_CASE, `@SerialName("snake_case")` matching Telegram API
- **ProcessedUpdate subclass**: `XxxUpdate` (e.g. `CallbackQueryUpdate`)

## Reference Examples

- [MessageUpdate](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/ProcessedUpdate.kt) - UserReference + ChatReference
- [PollUpdate](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/ProcessedUpdate.kt) - minimal, no User/Chat
- [CallbackQueryUpdate](telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/types/component/ProcessedUpdate.kt) - UserReference + ChatReference

## Validation

```bash
./gradlew formatKotlin
./gradlew prepareRelease
```
