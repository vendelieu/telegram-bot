# Telegram-bot changelog

### 2.3.1

* Returned `inputListener` parameter to bot instance.
* Add `HttpLogLevel` proxy class for http logs managing.

## 2.3.0

* Add `messageThreadId`,`isTopicMessage`, `forumTopicCreated`, `forumTopicClosed`, `forumTopicReopened` to `Message`
* Created new related types `ForumTopicCreated`, `ForumTopicClosed`, `ForumTopicReopened`
* Add `isForum`, `activeUsernames`, `emojiStatusCustomEmojiId` to `Chat`
* Add `canManageTopics` to `PromoteChatMemberOptions`, `ChatAdministratorRights`, `ChatMember`, `ChatPermissions`
* Add `messageThreadId` to `OptionsCommon` interface and all related options


* Fixed a bug throwing `IllegalArgumentException` caused by `ClassManagerImpl`
* Added saving instances to the `ClassManagerImpl`, to improve memory handling
* Made `ChatPermissions` mutable to fix some functions wrong logic.
* Added handling of commands containing at character, i.e. such as `/command@bot`
* Expanded and moved configuration of `TelegramBot` instance to more handy variant, see `BotConfiguration` in docs.
* Upgrade dependencies versions:
    * `Logback`: `1.4.0` -> `1.4.4`
    * `Jackson`: `2.13.4` -> `2.14.0`
    * `Ktor`: `2.1.0` -> `2.1.3`
    * `Kotlin`: `1.7.10` -> `1.7.20`

### 2.2.2

* Change ActionRecipientRef class to Recipient and add universal `from` method
* Fix `forwardMessage`, `copyMessage` methods, add required chatId parameters

### 2.2.1

* Process actions from callback requests in manual mode.
* Upgrade dependencies versions:
    * `Logback`: `1.2.11` -> `1.4.0`
    * `Jackson`: `2.13.3` -> `2.13.4`
    * `Ktor`: `2.0.3` -> `2.1.0`

## 2.2.0

* Fixed error response additional parameter name and its parameters optionality bug.
* Move parameters from `Feature` interface to separate `ParametersBase` interface so as not to overload unnecessary
  entities.
* Remove `isInline` parameter from `send` methods and moved it \
  to separate interface `InlineMode` to leave it only at the necessary
  methods and not to overload the methods that do not have inline mode.
* Make ManualHandlingBehaviour parameter lazy, is made to optimize cases where only annotation processing is used.
* Moved options saving mechanism from helper functions to mapper, for more consistency.
* Improved and optimized `MediaAction` mechanism, make it less verbose.
* Moved samples to separate repository.

## 2.1.0

* Remove `containMasks` and add `stickerType` to `StickerSet`, `CreateNewStickerSet`.
* Add new `CustomEmoji` to `EntityType`.
* Add `customEmojiId` to `MessageEntity`.
* Add `hasRestrictedVoiceAndVideoMessages` to `Chat`.
* Add new api method `getCustomEmojiStickers`.
* Fixed bug in `InputMedia` with nonNullability of `thumb` and `parseMode` in `String` type.

### 2.0.1

* Upgrade `Kotlin`: `1.6.21` -> `1.7.10`.
    * `Dokka`: `1.6.21` -> `1.7.10`.
* Upgrade `kotlinx-coroutines-core`: `1.6.2` -> `1.6.4`.
* Changed in `ManualHandlingDsl` input context from just Update to `InputContext`.

# 2.0.0

* Change annotations name from `TelegramCommand`, `TelegramInput`, `TelegramUnhandled`, `TelegramParameter` \
  to `CommandHandler`, `InputHandler`, `UnprocessedHandler`, `CallbackParam`.
* Change `bot.input` to `bot.inputListener`.
* Change `BotWaitingInput` to `BotInputListener`.
* Move serde logic from ktor setting to manual.
* Improved and reworked `ManualHandlingDsl`, added the ability to input chaining.
* The concept of contexts has been added to the manual processing.

### 1.5.2

* Returned single update parsing method.

### 1.5.1

* Improved structure of `Response` type.
* Fixed `List<>` serialized error.

## 1.5.0

* Delete old deprecated methods.
* Added shortcuts to process updates `handleUpdates()`(for annotation handling) and `handleUpdates{}`(for manual
  handling). \
  Before that, only a more verbose and detailed mechanism was available via `bot.update.setListener{}` &
  `bot.update.handle`(which is still available for more detailed process control.).
* Now the action search path has become nullable to be able to use the bot only in manual mode.

### 1.4.2

* Add `sender` to `ChatType`.
* Improved and optimized `MediaAction` mechanism.
* Add new `ImplicitFile` type so as not to create constructors with double input parameters.
* Fixed bug with sending optional parameters in media requests.

### 1.4.1

* Make coroutines dependency transitive.
* Fix parameter name in `VideoNoteOptions`.

## 1.4.0

* Add new api method `createInvoiceLink`.
* Change name of interface `IOptionsCommon`, `IFileOption` to `OptionsCommon`, `FileOptions`.
* Change `fileSize` type from `Int` to `Long` due to api changes.
* Add `joinToSendMessages`, `joinByRequest` parameters to `Chat`.
* Add `premiumAnimation` parameter to `Sticker`.
* Add `isPremium`, `addedToAttachmentMenu` parameter to `User`.
* Add `secretToken` parameter to `SetWebhookOptions`.

### 1.3.6

* Add update parsing method `parseUpdate` in `TelegramUpdateHandler`.
* Add async methods to `BotChatData`, `BotUserData`, `BotWaitingInput`.

### 1.3.5

* Fix bug with content-type when sending media request.

### 1.3.4

* Fix `MediaAction` wrong recipient bug.

### 1.3.3

* Publishing process fixes.

### 1.3.2

* Fix `captionEntities` ambiguity.

### 1.3.1

* Move package from `com.github.vendelieu` to `eu.vendeli`
* Change target repository from `jitpack` to `mavenCentral`.
* Add trace logging at some points
* Improve some documentation

## 1.3.0

* Added the ability to process requests manually with `ManualHandlingDsl`.
* Upgrade `Ktor`: `2.0.1` -> `2.0.2`.

## 1.2.2

* Added functionality for adding a user-defined "magic" objects.
* Improved `SetMyCommands` method by adding a more convenient dsl to add commands with `BotCommandsBuilder`.
* To add an `MessageEntity` in `EntityInterface`, an additional method has also been added that works.
  through `EntityBuilder`.
* Upgrade `reflections`: `0.9.12` -> `0.10.2`.

### 1.1.2

* Fix serde process of sealed classes `BotCommandScope`, `ChatMember`, `InlineQueryResult`, `InputMedia`, `MenuButton`
  , `PassportElementError`.

### 1.1.1

* Change commands/inputs search path declaring from `Package` to `String`.
* Optimize code, make some parts reusable.

## 1.1.0

* Make parameters value nullable.
* Change `bot.updateHandler` to `bot.update`, to avoid tautology when calling `bot.updateHandler.handleUpdate`, etc.
* Change `bot.inputHandler` to `bot.input` for same reason.

# 1.0.0

The initial public version.

Before that, the library was written for personal purposes and started with a couple of wrapper methods and as needed.