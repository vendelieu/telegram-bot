# Telegram-bot changelog

### 3.0.1

* Fixed behavior when no action was found and regex processing threw an exception if the received message did not match.

# 3.0.0

* Added ability for lib to catch actions in top level functions and object functions.
* Fixed wrong action bugs in forum related actions.
* Fixed `GetGameHighScores`, `GetMyShortDescription` methods.
* Changed `Recipient` class that used in some action to `Identifier`
* Covered `6.8` version of Tg Api.
    * Added new method `unpinAllGeneralForumTopicMessages.`.
    * Added new field `story` to `Message`.
    * Added new field `emojiStatusExpirationDate` to `Chat`.
    * Added new field `voterChat` to `PollAnswer`.

## 2.9.0

* Added new annotation `RegexCommandHandler` for handling regex commands.
* Changed `MagicObject` into `Autowiring` for better understanding.
* Added ability for passing `MessageUpdate` and `CallbackQueryUpdate` into function (instead more
  generic `ProcessedUpdate`, passing which is also possible).

### 2.8.1

* Bumped dependencies versions
* Little internal logic improvements

## 2.8.0

#### Telegram Api related changes

* Change `switchPmText` `switchPmParameter` to `button` with `InlineQueryResultsButton` in `answerInlineQuery`.
* Added the field `webAppName` to the class `WriteAccessAllowed`.
* Added the ability to set different bot names for different user languages using the method `setMyName`.
* Added the ability to get the current bot name in the given language as the class `BotName` using the
  method `getMyName`.
* Added the field `switchInlineQueryChosenChat` of the type `SwitchInlineQueryChosenChat` to the
  class `InlineKeyboardButton`.
* Added the field `viaChatFolderInviteLink` to the class `ChatMemberUpdated`.

#### Bot improvements

* Deleted deprecated `Bot*` prefix for Context interfaces. #22
* Added default implementation for context interfaces based on `ConcurrentHashMap`. #25
* Make `MagicObject.get` function suspendable. #19
* Add scope parameter for manual commands. #20
* Add additional parsing for deeplink case.

### 2.7.4

* Fix `Voice` wrong parameter type.
* Fix `ForceReply` wrong parameter name.

### 2.7.3

* Swap parameters in `chatAction` method for better interface.
* Exposed `Update.processUpdate()` method.
* Improved logging (better layer separation, error notification even in silent mode).
* Make `restrictSpacesInCommands` - `false` by default.
* Add missing `switchInlineQueryCurrentChat` parameter to `InlineKeyboardButton`.
* Fixed `ManualDsl.whenNotHandled`.
* Fixed `InputChaining` logic.
* Refactor and improve commandParsing logic.

### 2.7.2

* Reworked `ProcessedUpdate` to sealed structure for more consistency.
* Deprecated Bot* prefix of contextual interfaces to use them without prefix.
* Added new `scope` parameter to `@CommandHandler` annotation, to be able to isolate the processing.

### 2.7.1

* Added new `ConfigLoader` interface and ability to initialize a `TelegramBot` instance through it.
  The default implementation of configuring via environment variables is present.
* A lowered version of the reflection library, due to problems with finding actions on some environments.

## 2.7.0

#### Telegram Api related changes

* Add `setMyDescription`, `getMyDescription`, `setMyShortDescription`, `getMyShortDescription` methods.
* Add `emoji` parameter to `sendSticker`.
* Add sticker set related methods and add support of `webp` format.
* Renamed `thumb` to `thumbnail` in places where it was present.

#### Bot improvements

* Add new contextual entities building.
* Renamed `CallbackParam` annotation to `ParamMapping`.
* Reorganized types structure (some imports may have broken because of it).
* Unnecessary elements (parameters, options etc.) from actions have been hidden from the user interface.

## 2.6.0

#### Telegram Api related changes

* Added the class `KeyboardButtonRequestUser` and the field `requestUser` to the class `KeyboardButton`.
* Added the class `KeyboardButtonRequestChat` and the field `requestChat` to the class `KeyboardButton`.
* Added the classes `UserShared`, `ChatShared` and the fields `userShared`, and `chatShared` to the class `Message`.
* Replaced the fields `canSendMediaMessages` in the classes `ChatMemberRestricted` and `ChatPermissions`
  with separate fields `canSendAudios`, `canSendDocuments`, `canSendPhotos`, `canSendVideos`, `canSendVideoNotes`,
  and `canSendVoiceNotes` for different media types.
* Added the parameter `useIndependentChatPermissions` to the methods `restrictChatMember` and `setChatPermissions`.
* Added the field `userChatId` to the class `ChatJoinRequest`.

#### Bot internal improvements

* Add new `UpdateHandler` annotation to handle update events.
* Improved command parsing mechanism and make it more flexible and configurable.
  See `commandParsing` section of bot configuration.
* Upgrade dependencies versions:
    * `Jackson`: `2.14.1` -> `2.14.2`
    * `Ktor`: `2.2.1` -> `2.2.3`
    * `Kotlin`: `1.8.0` -> `1.8.10`

### 2.5.4

* Fix test and improve `inputChain` logic.

### 2.5.3

* Add repeat parameter for `InputChain.breakIf()`

### 2.5.2

* Add new method for defining behaviour of update processing.
  to avoid redefining in webhook processing.

### 2.5.1

* Add method for only text buttons into `replyKeyboardButton()` builder.
* Fix not passing isPersistent parameter in `replyKeyboardMarkup()` builder.
* Pass `Update` next to exception in `TelegramUpdateHandler.caughtExceptions`.

## 2.5.0

#### Telegram Api related changes

* Add the field `isPersistent` to the class `ReplyKeyboardMarkup`.
* Add `hasSpoiler` to `photo()` `video()` `animation()` options and into `InputMedia.Photo` `InputMedia.Video`
  `InputMedia.Animation`. Also `hasMediaSpoiler` to `Message`.
* Add `hasHiddenMembers`, `hasAggressiveAntiSpamEnabled` parameters to `Chat`.
* Add messageThreadId parameter to `chatAction()`.
* Add `forumTopicEdited`, `generalForumTopicHidden`, `generalForumTopicUnhidden`, `writeAccessAllowed` to `Message`.
* Add topic management methods.

#### Bot internal improvements

* Deleted some too verbose constructors of `ReplyKeyboardMarkup`.
* Created new convenient DSL for creating markup - `replyKeyboardMarkup()`, similar to inline keyboard version, see
  examples in wiki.
* A new parameter `TelegramUpdateHandler.caughtExceptions` was introduced, where centralized caught errors will be sent.
  Exceptions that occurred during manual update processing are now also intercepted.
* Changed `invoice()`(`sendInvoice`), `createInvoiceLink`(`createInvoiceLink`) parameters defining by moving it to
  lambda. For more convenience and less verbosity.
* Changed `createNewStickerSet()`(`createNewStickerSet`) parameters defining by moving it to lambda.
* Improved entity defining in `EntitiesBuilder` moving optional parameters to lambda.
* Add generics to `BotContext`(`chatData`, `userData`) interfaces.
* Deleted private `Update` extensions for handling and move string update handling to new
  function `TelegramUpdateHandler.parseAndHandle`.
* Add some update listener configuring variables to `TelegramBot.botConfiguration` - `updatesListener()`.
* Moved `BotContext`(`chatData`, `userData`) configuring to `TelegramBot.botConfiguration` - `context()`.
  And now they are not null, but you can get an exception if you try to access without setting.
* Upgrade dependencies versions:
    * `Gradle`: `7.5.0` -> `7.6.0`
    * `Jackson`: `2.14.0` -> `2.14.1`
    * `Ktor`: `2.1.3` -> `2.2.1`
    * `Kotlin`: `1.7.21` -> `1.8.0`

### 2.4.2

* Fix bug when in the media request additional options violates the query itself.
* Refactor MediaGroup action, implement a separate processing mechanism from usual media actions that takes into account
  the possibility of sending different types of attachments (string, bytearray, file).

### 2.4.1

* Fix annotation limits not working

## 2.4.0

* Added a new mechanism to limit requests from users, both general requests and specifically for commands/inputs in both
  modes (annotations, manual).
* Long data types are replaced by shorter ones using aliases.
* Upgrade dependencies versions:
    * `Logback`: `1.4.4` -> `1.4.5`
    * `Kotlin`: `1.7.20` -> `1.7.21`

### 2.3.2

* Fix class initialization bug.

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

* The initial public version.

Before that, the library was written for personal purposes and started with a couple of wrapper methods and as needed.