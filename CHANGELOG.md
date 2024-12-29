# Telegram-bot (KtGram) Changelog

#### [unreleased]

* Added `ktorJvmEngine` parameter to plugin with option to choose ktor engine.
* Added `onHandlerException` for spring starter bot configuration, to cover exceptions thrown by update handler.
* Added extenstion for `String` for escaping formatting, `escapeFormatting(parseMode)`, `escapeMarkdown`,
  `escapeMarkdownV2`, `escapeHTML`.

### 7.6.2

* Move all sealed structure serialization to unified approach.

### 7.6.1

* Fixed `ChatMemberUpdated` serde issue.

## 7.6.0

* Supported Telegram API [8.1](https://core.telegram.org/bots/api-changelog#december-4-2024).
* Fixed `InlineQueryResult` cached and usual results serde clashing issue.
* Fixed `savePreparedInlineMessage` options serde issue.
* Added option to disable automatically cleaning class data through plugin's `autoCleanClassData` parameter.

## 7.5.0

* Supported Telegram API [8.0](https://core.telegram.org/bots/api-changelog#november-17-2024).
* Extend/add annotations for utility handler mechanisms (guard, rateLimits, argParser), add ability to tag a class to
  affect all methods within it.
* Added `autoAnswer` to `@CommandHandler.CallbackQuery`, to answer callback queries automatically before handler.
* Logging has been moved to the ktor logging utility (nothing much has changed, just removed some unnecessary
  dependencies).
* `WebApp.initData` check `String` extension function moved to `ktgram-utils` module.

### 7.4.1

* Fixed KSP processor exception that was caused by new `KotlinPoet` version update.

## 7.4.0

* Supported Telegram API [7.11](https://core.telegram.org/bots/api-changelog#october-31-2024).
* Removed `onCommand(Regex)` from FunctionalDSL since `common` is already covering its case and the functionality was
  removed anyway and the feature remained by chance :)
* Exposed updates flow `bot.update.flow` [🔬].
* Little improvements to logging process.
* Added `linuxX64` + `mingwX64` targets to `botctx-redis` module.

### 7.3.1

* Add missing `user` and `text` references for `PurchasedPaidMediaUpdate`.

## 7.3.0

* Supported Telegram API [7.10](https://core.telegram.org/bots/api-changelog#september-6-2024).
* Added option to auto clean state in `@InputChain` as annotation parameter [🔬].
* Added option to set chaining strategy for `@InputChain` links, to have more control over flow.
* Added option to set custom argument parser for specific activities (`@CommandHandler`, `@CommonHandler`).
* Removed special deeplink case handling, now will need to use the basic mechanism (can be caught by ordered
  parameters).

### 7.2.2

* Added an option to define custom keys for stateful links.

### 7.2.1

* Fixed KSP applying flow in the plugin.

## 7.2.0

* Introduced a new plugin for more convenient library installation.
* Transitioned from `ChainStateManager` to `StatefulLink` flow for improved convenience.

## 7.1.0

* Supported Telegram API [7.9](https://core.telegram.org/bots/api-changelog#august-14-2024).
* Added `ChainStateManager.getState` extension to retrieve state via a link.

### 7.0.1

* Fixed KSP generation artifact.

# 7.0.0

#### Common

* Reorganized internal structure (mostly interfaces and some types), potentially breaking imports.
* Added experimental method `TelegramBot.getInstance` to obtain an instance through `ClassManager`.
* Removed `EnvConfigLoader` from the main module due to low demand.
* Added bot parameter to `spring-starter`'s `onInit` hook.
* Exposed companion objects for `TelegramBot` and `TgUpdateHandler` for static custom extensions.
* Upgraded JVM target version from `11` to `17`.

#### BotContext

* Reworked base methods of `BotContext` for stronger typing.
* Renamed `chatData` to `classData` for functional appropriateness.
* Decoupled `BotContext` (`userData`, `classData`) from the bot.
  New annotation `@CtxProvider` allows custom implementation.
* Added operator extensions `get` and `set` for `User` class with `userData`, enabling usage like
  `user["key"] = "value"`.

#### Fail State Handling

* Added `exceptionHandlingStrategy` to `configuration` for more control over exception handling.
* Added `throwExOnActionsFailure` to `configuration` to throw exceptions on API request failures.
* Added `retryOnTooManyRequests()` to httpClient `configuration`, offering a `RetryStrategy` for API requests with
  status `429`.

#### Modules

* Updated method extensions to `TelegramBot` from `ktgram-utils` module, removing `Sc` postfix and adding all
  variations.
* Added experimental module (jvmOnly) `ktgram-botctx-redis`, a `Redis` implementation of `BotContext` (`userData`,
  `classData`).
* Added experimental module `ktgram-config-env`, similar to the removed `EnvConfigLoader` but for all supported
  platforms.
* Added experimental module `ktgram-config-toml`, allowing `TelegramBot` configuration through a TOML file.

#### Other

* Added `logger` to `LoggingConfiguration` for any `Logger` interface implementation.
* Added `@InputChain` state manager for automatic state selection from updates.
* Enhanced API validation across the project and corrected parameter typos.

## 6.6.0

* Supported Telegram API [7.8](https://core.telegram.org/bots/api-changelog#july-31-2024).

### 6.5.1

* Fixed `httpClient` initialization exception in the spring starter.

### 6.5.0

* Added `button(...)` function for defining a button in `answerInlineQuery` options.
* Added `toWebhookResponse` method to actions for webhook responses.
* Added separate `bot.update.parse(...)` for webhook responses.
* Fixed incorrect request URL resolution with multiple bot instances.
* Enabled `httpClient` sharing between instances in ktor starter; spring starter also uses `httpClient` if a bean is
  defined.

### 6.4.1

* Fixed `SuccessfulPayment` `orderInfo` nullability bug.

## 6.4.0

* Changed a request URL forming scheme, allowing protocol setting and adding `isTestEnv` parameter for testing
  environments.
* Added `bot.update.runExceptionHandler {}` for exception handling loops.
* Added secondary constructors for `ImplicitFile` with `String` and `InputFile` options.
* Added `.sendReturning()` method (similar to `sendAsync()`) for clearer flow.
* Moved `user_id` parameter to constructor in methods like `createNewStickerSet`, reflecting Telegram API more
  accurately.
* Improved optional `thumbnail` passing for media actions (photo, document, etc.).

### 6.3.1

* Fixed `webapp` onEvent handling.
* Changed `Update` usage to `ProcessedUpdate` in processes (also in `bot.handleUpdates()`,
  `bot.update.caughtExceptions`).
* Changed `replyKeyboardMarkup {}` webApp right operand to URL string.

## 6.3.0

* Supported Telegram API [7.7](https://core.telegram.org/bots/api-changelog#july-7-2024).

## 6.2.0

* Supported Telegram API [7.6](https://core.telegram.org/bots/api-changelog#july-1-2024).
* Added `onInit` hook in spring-boot starter.
* Fixed a rare issue with sealed structure serialization.
* Changed `ProcessedUpdate` source update parameter from `update` to `origin`; the old one remains but is deprecated.

### 6.1.1

* Fixed `String` non-nullability issue in KSP processor.
* Fixed `chatData` clearing behavior within the same class.
* Changed `date` type from long to `Instant` in `RevenueWithdrawalState` and `StarTransaction`.
* Changed `TransactionPartner.User` to `TransactionPartner.UserPartner` to avoid confusion with base `User` resolution.
* Added experimental `Collection<>.joinToInlineKeyboard` function for basic pagination.

## 6.1.0

* Supported Telegram API [7.5](https://core.telegram.org/bots/api-changelog#june-18-2024).

### 6.0.1

* Fixed webapp `setParams` immutability.
* Fixed KSP `UpdateType` resolution problem.
* Fixed `photoHeight` missing in `InlineQueryResult`.

# 6.0.0

* Introduced `@CommonHandler` with nested annotations `@CommonHandler.Text` and `@CommonHandler.Regex` for rich
  configuration.
* Removed `@RegexCommandHandler`, replaced by `@CommonHandler.Regex`.
* Changed guard interface from `Filter` to `Guard` due to `@CommonHandler` filtering mechanics.

## 5.5.0

* Added new guard mechanism to `@CommandHandler` (+`@CallbackQuery`) and `@InputHandler` for pre-processing checks.
* Added `beforeAction` and `afterAction` hooks to `ChainLink`. #153
* Added `ksp2` support; enable with `ksp.useKSP2=true` in gradle.properties.
* Fixed missing `chatId` in `BusinessExt` action.
* Fixed `TgUpdateHandler` logging level mismatch. #149
* Fixed `replyKeyboardMarkup` missing option changes. #152
* Supported Telegram API [7.4](https://core.telegram.org/bots/api-changelog#may-28-2024).

## 5.4.0

* Supported Telegram API [7.3](https://core.telegram.org/bots/api-changelog#may-6-2024).
* Enabled custom `ChainLink` implementation in `InputChain`.
* Improved Spring starter configuration.
* Removed old `ListingBuilder` methods for multiple elements; use `addAll()`.
* Added new builder for `sendPoll`/`poll` options.

### 5.3.2

* Fixed `EntitiesContextualBuilder` bug.

### 5.3.1

* Fixed incorrect class manager in spring starter configuration.
* Enabled manual instance configuration.

## 5.3.0

* Fixed exception throwing bug in `ActionExt(Inline, Business)`.
* Added parameter to `@RegexCommandHandler` for regex options.
* Introduced experimental Spring starter.

## 5.2.0

* Added `ktor-starter` module for quick webhook server setup.
* Added `identifier` parameter for `TelegramBot` instance.
* Changed `kotlinx-datetime` to transitive dependency due to its use in parameters.
* Restored a missing inline mode action extension for `getGameHighScores` method.
* Added `inputAutoRemoval` parameter to configuration for precise `inputListener` flow control.
* Added missing 7.2 API `BiometricManager` to webapps.

## 5.1.0

* Fixed redundant quotation in multipart requests.
* Restored `logback` as logger for JVM target.
* Moved inline mode methods to extension interface from separate `InlinableAction`.
* Added `Any` upper bounds for `Autowiring` interface to prevent incorrect behavior.
* Supported Telegram API [7.2](https://core.telegram.org/bots/api-changelog#march-31-2024) changes.

### 5.0.5

* Fixed nullable type resolution issue in `Autowiring` mechanics.
* Changed default Ktor engine to java-http.

### 5.0.4

* Removed lazy structures causing performance issues.

### 5.0.3

* Fixed missing `user` parameter in functional handlers.

### 5.0.2

* Fixed KSP processor for regex handlers. #106
* Fixed KSP processor `InputHandler` and `RegexHandler` rate limits collection.

### 5.0.1

* Changed internal modifier for `context {}` config section. #103
* Fixed annotation processing error in KSP processor for non-JVM modules.
* Added basic `ClassManager` for Kotlin Native target.

# 5.0.0

* Transitioned a library to Kotlin Multiplatform with `jvm`/`js`/`native` goals (experimental).
* Replaced a serialization library with `kotlinx-serialization`.
* Updated logging utility.
* Added webapp module.
* Moved message-related methods to a separate package (imports may be broken).

### 4.3.1

* Fixed coroutines space overconsumption bug.

## 4.3.0

* Renamed internal components:
    * `Action` (annotated entities) is now `Activity`, to avoid confusion with request-forming actions.
    * `ManualHandling` is now `FunctionalHandling` for clarity.
* Added `onFailure` shortcut method on `Deferred` for consistency.
* Logged `Activity` list during initialization.
* Improved internal structure of `FunctionalHandling`.
* Enhanced coroutine flow in long polling mode; added request timeout in configuration (`updatesListener` block).
* Improved retry processing in a client to avoid unnecessary request repetition (e.g., status 400).
* Changed `messageThreadId` parameter type to `Int` for better data representation.
* Simplified base scope of commands to `UpdateType.Message` for beginner friendliness.

### 4.2.1

* Removed unnecessary `bot.addAutowiringObject` method.
* Enhanced `userData`/`chatData` get method with generic wrapping for safety.
* Added `allowedUpdates` for long-polling to receive special updates.
* Fixed `giveawayMessageId` type: `Int` to `Long`.
* Added `replyParameters(messageId, block: ReplyParameters.() -> Unit)` shortcut for modifying reply parameters.
* Optimized internal type inference for actions.

## 4.2.0

* Added `send` method for `Chat` to `Action<>`.
* Renamed ambiguous `User`/`Chat` names:
    * `MessageOrigin` (`User` to `UserOrigin`, `Chat` to `ChatOrigin`)
    * `BotCommandScope.Chat` to `BotCommandScope.ChatScope`
* Enhanced `linkPreviewOptions`: Introduced `disableWebPagePreview()` and `linkPreviewOptions{}` for lambda setting.
* Improved sealed class structure for better accessibility to general parameters (`MaybeInaccessibleMessage`,
  `MessageOrigin`, `ChatBoostSource`, `ChatMember`).
* Removed `ReflectionHandler` as `CodegenHandler` is effective.
* Separated updates collection and handling in long-polling.
* Restored `coroutines` dependency as transitive for `sendAsync` methods.
* Added `getOrNull()` method for `Deferred<Response<T>>` to reduce verbosity.
* Set default names for media methods using `ByteArray` and `File` to prevent incorrect API behavior.
* Changed `botLogLevel` type to new proxy enum `LogLvl` since `logback` is not transitive.

## 4.1.0

* Introduced `@CommandHandler.CallbackQuery` shortcut annotation (or import without a prefix).
* Changed all date fields representing timestamps to `Instant` and periods to `Duration`.
* Changed `caughtExceptions` event structure from `Pair` to `FailedUpdate`.
* Changed `logback` and `coroutines` from API dependency to implementation-dependency (non-transitive).
* Supported Telegram API 7.0.

### 4.0.1

* Fixed parsing annotations bug with unordered parameters.

# 4.0.0

* Enable an action collection at compile time.
* Enhanced support for input chains in annotation mode.
* Supported all command scopes.
* Allowed all `ProcessedUpdate` subclasses in new codegen update handler.
* Improved signatures of methods like `copyMessage`, `forwardMessage`, `banChatSenderChat`, `unbanChatSenderChat`.

## 3.5.0

* Added infix functions to `EntitiesBuilder` (used in `caption {}` and `entities {}`).
* Added infix function to builder used in `setMyCommands` for easier command addition.
* Changed methods like `answerInlineQuery`, `answerShippingQuery`, `poll`, `setPassportDataErrors` to use
  `ListingBuilder` for convenience.
* Added `User` shortcuts where `userId` is used in function-methods.
* Added method shortcuts where the `send*` keyword was omitted.
* Refactored internal structure of actions and features.

## 3.4.0

* Added `InputChaining` in annotation mode as an experimental feature.
* Added `additionalHeaders` for requests in `HttpConfiguration` (useful for socks proxy authorization).
* Added `Deffered<Response<T>>.foldResponse()` function for async responses.

### 3.3.1

* Enabled proxy usage. See `httpClient{}` section in bot configuration.

## 3.3.0

* Fixed missing media in an input handling process.
* Consolidated rate limit mechanism settings into `rateLimiter{}` configuration; set rate exceeded action in
  configuration.
* Added convenient shortcuts for `InputListener` and `BotContext` interfaces.
* Minor internal code optimizations and library version updates.

## 3.2.0

* Added `fromRequest` and `fromAttachmentMenu` fields to `WriteAccessAllowed`.
* Added new admin privileges `canPostStories`, `canEditStories`, and `canDeleteStories` to `ChatMember.Administrator`
  and `ChatAdministratorRights`.
* Added `canPostStories`, `canEditStories`, and `canDeleteStories` parameters to `promoteChatMember()`.

## 3.1.0

* Changed `Autowiring` and `ClassManager` interfaces to functional interfaces.
* Added extensions to `ProcessedUpdate` class:
    * `userOrNull` returns `User` or `null`.
    * `getUser` returns `User` or throws NPE (nullable in rare cases; use with caution).
* Added shortcut functions to `Markup` interfaces for `InlineKeyboardMarkup`, `ReplyKeyboardMarkup`, `ForceReply`.

### 3.0.4

* Improved signatures of some sticker-set and chat-related methods.

### 3.0.3

* Reworked multipart actions logic and fixed related bugs.

### 3.0.2

* Aligned manual handling flow with annotation regarding regex processing.

### 3.0.1

* Fixed behavior when no action was found, and regex processing threw an exception if the message did not match.

# 3.0.0

* Enabled lib to catch actions in top-level and object functions.
* Fixed wrong action bugs in forum-related actions.
* Fixed `GetGameHighScores`, `GetMyShortDescription` methods.
* Changed `Recipient` class to `Identifier`.
* Supported `6.8` version of Telegram API:
    * Added `unpinAllGeneralForumTopicMessages` method.
    * Added `story` field to `Message`.
    * Added `emojiStatusExpirationDate` to `Chat`.
    * Added `voterChat` to `PollAnswer`.

## 2.9.0

* Added `RegexCommandHandler` annotation for regex commands.
* Changed `MagicObject` to `Autowiring` for clarity.
* Enabled passing `MessageUpdate` and `CallbackQueryUpdate` into function (in addition to `ProcessedUpdate`).

### 2.8.1

* Updated dependencies versions.
* Minor internal logic improvements.

## 2.8.0

#### Telegram API Changes

* Changed `switchPmText` and `switchPmParameter` to `button` with `InlineQueryResultsButton` in `answerInlineQuery`.
* Added `webAppName` field to `WriteAccessAllowed`.
* Enabled setting different bot names for different user languages using `setMyName`.
* Added method `getMyName` to retrieve current bot name in a given language as `BotName`.
* Added `switchInlineQueryChosenChat` field to `InlineKeyboardButton`.
* Added `viaChatFolderInviteLink` field to `ChatMemberUpdated`.

#### Bot Improvements

* Removed deprecated `Bot*` prefix from Context interfaces. #22
* Added default implementation for context interfaces based on `ConcurrentHashMap`. #25
* Made `MagicObject.get` function suspendable. #19
* Added scope parameter for manual commands. #20
* Added additional parsing for deeplink cases.

### 2.7.4

* Fixed `Voice` wrong parameter type.
* Fixed `ForceReply` wrong parameter name.

### 2.7.3

* Swapped parameters in `chatAction` method for better interface.
* Exposed `Update.processUpdate()` method.
* Enhanced logging (better layer separation, error notification even in silent mode).
* Made `restrictSpacesInCommands` `false` by default.
* Added missing `switchInlineQueryCurrentChat` parameter to `InlineKeyboardButton`.
* Fixed `ManualDsl.whenNotHandled`.
* Fixed `InputChaining` logic.
* Refactored and improved command parsing logic.

### 2.7.2

* Reworked `ProcessedUpdate` to a sealed structure for consistency.
* Deprecated `Bot*` prefix for contextual interfaces.
* Added new `scope` parameter to `@CommandHandler` annotation for isolated processing.

### 2.7.1

* Added `ConfigLoader` interface and ability to initialize a `TelegramBot` instance through it.
* Lowered reflection library version due to action-finding issues in some environments.

## 2.7.0

#### Telegram API Changes

* Added methods `setMyDescription`, `getMyDescription`, `setMyShortDescription`, `getMyShortDescription`.
* Added `emoji` parameter to `sendSticker`.
* Added sticker set related methods and `webp` format support.
* Renamed `thumb` to `thumbnail` where applicable.

#### Bot Improvements

* Added new contextual entities building.
* Renamed `CallbackParam` annotation to `ParamMapping`.
* Reorganized types structure (some imports may be broken).
* Hid unnecessary elements (parameters, options, etc.) from the user interface.

## 2.6.0

#### Telegram API Changes

* Added `KeyboardButtonRequestUser` class and `requestUser` field to `KeyboardButton`.
* Added `KeyboardButtonRequestChat` class and `requestChat` field to `KeyboardButton`.
* Added `UserShared`, `ChatShared` classes and `userShared`, `chatShared` fields to `Message`.
* Replaced `canSendMediaMessages` in `ChatMemberRestricted` and `ChatPermissions` with separate fields for different
  media types.
* Added `useIndependentChatPermissions` parameter to `restrictChatMember` and `setChatPermissions`.
* Added `userChatId` field to `ChatJoinRequest`.

#### Bot Internal Improvements

* Added `UpdateHandler` annotation for update events.
* Improved command parsing mechanism for flexibility and configurability (see `commandParsing` in bot configuration).
* Updated dependencies:
    * `Jackson`: `2.14.1` -> `2.14.2`
    * `Ktor`: `2.2.1` -> `2.2.3`
    * `Kotlin`: `1.8.0` -> `1.8.10`

### 2.5.4

* Fixed and improved `inputChain` logic.

### 2.5.3

* Added repeat parameter for `InputChain.breakIf()`.

### 2.5.2

* Added method for update processing behavior definition to avoid redefinition in webhook processing.

### 2.5.1

* Added text button method to `replyKeyboardButton()` builder.
* Fixed `isPersistent` parameter passing in `replyKeyboardMarkup()` builder.
* Passed `Update` next to exception in `TelegramUpdateHandler.caughtExceptions`.

## 2.5.0

#### Telegram API Changes

* Added `isPersistent` field to `ReplyKeyboardMarkup`.
* Added `hasSpoiler` to `photo()`, `video()`, `animation()` options and `InputMedia.Photo`, `InputMedia.Video`,
  `InputMedia.Animation`. Also added `hasMediaSpoiler` to `Message`.
* Added `hasHiddenMembers`, `hasAggressiveAntiSpamEnabled` parameters to `Chat`.
* Added `messageThreadId` parameter to `chatAction()`.
* Added `forumTopicEdited`, `generalForumTopicHidden`, `generalForumTopicUnhidden`, `writeAccessAllowed` to `Message`.
* Added topic management methods.

#### Bot Internal Improvements

* Deleted verbose `ReplyKeyboardMarkup` constructors.
* Created `replyKeyboardMarkup()` DSL for markup creation, similar to an inline keyboard version (see wiki for
  examples).
* Introduced `TelegramUpdateHandler.caughtExceptions` for centralized error handling.
* Changed `invoice()` (`sendInvoice`) and `createInvoiceLink` (`createInvoiceLink`) parameters to lambda for
  convenience.
* Changed `createNewStickerSet()` (`createNewStickerSet`) parameters to lambda.
* Improved entity definition in `EntitiesBuilder` by moving optional parameters to lambda.
* Added generics to `BotContext` (`chatData`, `userData`) interfaces.
* Deleted private `Update` extensions for handling and created `TelegramUpdateHandler.parseAndHandle`.
* Added update listener configuration variables to `TelegramBot.botConfiguration` - `updatesListener()`.
* Moved `BotContext` (`chatData`, `userData`) configuration to `TelegramBot.botConfiguration` - `context()`.
* Updated dependencies:
    * `Gradle`: `7.5.0` -> `7.6.0`
    * `Jackson`: `2.14.0` -> `2.14.1`
    * `Ktor`: `2.1.3` -> `2.2.1`
    * `Kotlin`: `1.7.21` -> `1.8.0`

### 2.4.2

* Fixed the media request bug where additional options violated the query.
* Refactored `MediaGroup` action, implementing separate processing for different attachment types (string, bytearray,
  file).

### 2.4.1

* Fixed annotation limits not working.

## 2.4.0

* Introduced a request limiting mechanism for both general and specific command/input requests in both modes (
  annotations, manual).
* Replaced long data types with shorter aliases.
* Updated dependencies:
    * `Logback`: `1.4.4` -> `1.4.5`
    * `Kotlin`: `1.7.20` -> `1.7.21`

### 2.3.2

* Fixed class initialization bug.

### 2.3.1

* Restored `inputListener` parameter to bot instance.
* Added `HttpLogLevel` proxy class for HTTP log management.

## 2.3.0

* Added `messageThreadId`, `isTopicMessage`, `forumTopicCreated`, `forumTopicClosed`, `forumTopicReopened` to `Message`.
* Created related types `ForumTopicCreated`, `ForumTopicClosed`, `ForumTopicReopened`.
* Added `isForum`, `activeUsernames`, `emojiStatusCustomEmojiId` to `Chat`.
* Added `canManageTopics` to `PromoteChatMemberOptions`, `ChatAdministratorRights`, `ChatMember`, `ChatPermissions`.
* Added `messageThreadId` to `OptionsCommon` interface and related options.

* Fixed `IllegalArgumentException` in `ClassManagerImpl`.
* Improved memory handling by saving instances in `ClassManagerImpl`.
* Made `ChatPermissions` mutable to fix logic issues.
* Handled commands with `@` character, e.g., `/command@bot`.
* Expanded and moved `TelegramBot` instance configuration to `BotConfiguration` (see docs).
* Updated dependencies:
    * `Logback`: `1.4.0` -> `1.4.4`
    * `Jackson`: `2.13.4` -> `2.14.0`
    * `Ktor`: `2.1.0` -> `2.1.3`
    * `Kotlin`: `1.7.10` -> `1.7.20`

### 2.2.2

* Changed `ActionRecipientRef` class to `Recipient` and added universal `from` method.
* Fixed `forwardMessage` and `copyMessage` methods; added required `chatId` parameters.

### 2.2.1

* Processed actions from callback requests in manual mode.
* Updated dependencies:
    * `Logback`: `1.2.11` -> `1.4.0`
    * `Jackson`: `2.13.3` -> `2.13.4`
    * `Ktor`: `2.0.3` -> `2.1.0`

## 2.2.0

* Fixed error response parameter name and optionality bug.
* Moved parameters from `Feature` interface to `ParametersBase` to avoid unnecessary entities.
* Removed `isInline` parameter from `send` methods; moved to `InlineMode` interface for necessary methods.
* Made `ManualHandlingBehaviour` parameter lazy for optimization when only annotation processing is used.
* Move an option saving mechanism to mapper for consistency.
* Optimized `MediaAction` mechanism for verbosity reduction.
* Moved samples to a separate repository.

## 2.1.0

* Removed `containMasks` and added `stickerType` to `StickerSet` and `CreateNewStickerSet`.
* Added `CustomEmoji` to `EntityType`.
* Added `customEmojiId` to `MessageEntity`.
* Added `hasRestrictedVoiceAndVideoMessages` to `Chat`.
* Added new API method `getCustomEmojiStickers`.
* Fixed `InputMedia` bug with non-nullability of `thumb` and `parseMode` in `String` type.

### 2.0.1

* Updated `Kotlin`: `1.6.21` -> `1.7.10`.
    * `Dokka`: `1.6.21` -> `1.7.10`.
* Updated `kotlinx-coroutines-core`: `1.6.2` -> `1.6.4`.
* Changed `ManualHandlingDsl` input context from `Update` to `InputContext`.

# 2.0.0

* Renamed annotations:
    * `TelegramCommand` to `CommandHandler`
    * `TelegramInput` to `InputHandler`
    * `TelegramUnhandled` to `UnprocessedHandler`
    * `TelegramParameter` to `CallbackParam`
* Changed `bot.input` to `bot.inputListener`.
* Renamed `BotWaitingInput` to `BotInputListener`.
* Moved serde logic from Ktor setting to manual.
* Improved and reworked `ManualHandlingDsl` with input chaining.
* Introduced contexts to manual processing.

### 1.5.2

* Restored single update parsing method.

### 1.5.1

* Improved `Response` type structure.
* Fixed `List<>` serialization error.

## 1.5.0

* Deleted deprecated methods.
* Added shortcuts for processing updates: `handleUpdates()` (annotation handling) and `handleUpdates{}` (manual
  handling).
* Made action search path nullable for manual-only bot usage.

### 1.4.2

* Added `sender` to `ChatType`.
* Optimized `MediaAction` mechanism.
* Added `ImplicitFile` type to avoid double input parameter constructors.
* Fixed optional parameters sending bug in media requests.

### 1.4.1

* Made coroutines dependency transitive.
* Fixed parameter name in `VideoNoteOptions`.

## 1.4.0

* Added new API method `createInvoiceLink`.
* Renamed interfaces `IOptionsCommon` and `IFileOption` to `OptionsCommon` and `FileOptions`.
* Changed `fileSize` type from `Int` to `Long` due to API changes.
* Added `joinToSendMessages` and `joinByRequest` parameters to `Chat`.
* Added `premiumAnimation` parameter to `Sticker`.
* Added `isPremium` and `addedToAttachmentMenu` parameters to `User`.
* Added `secretToken` parameter to `SetWebhookOptions`.

### 1.3.6

* Added `parseUpdate` method in `TelegramUpdateHandler`.
* Added async methods to `BotChatData`, `BotUserData`, `BotWaitingInput`.

### 1.3.5

* Fixed media request content-type bug.

### 1.3.4

* Fixed `MediaAction` wrong recipient bug.

### 1.3.3

* Publishing process fixes.

### 1.3.2

* Fixed `captionEntities` ambiguity.

### 1.3.1

* Moved package from `com.github.vendelieu` to `eu.vendeli`.
* Changed target repository from `jitpack` to `mavenCentral`.
* Added trace logging at some points.
* Improved documentation.

## 1.3.0

* Added manual request processing with `ManualHandlingDsl`.
* Updated `Ktor`: `2.0.1` -> `2.0.2`.

## 1.2.2

* Added functionality for user-defined "magic" objects.
* Improved `SetMyCommands` method with `BotCommandsBuilder` DSL.
* Added method for `MessageEntity` addition through `EntityBuilder`.
* Updated `reflections`: `0.9.12` -> `0.10.2`.

### 1.1.2

* Fixed serde process of sealed classes like `BotCommandScope`, `ChatMember`, `InlineQueryResult`, `InputMedia`,
  `MenuButton`, `PassportElementError`.

### 1.1.1

* Changed commands/inputs search a path from `Package` to `String`.
* Optimized code and made reusable parts.

## 1.1.0

* Made parameters values nullable.
* Changed `bot.updateHandler` to `bot.update` to avoid tautology.
* Changed `bot.inputHandler` to `bot.input`.

# 1.0.0

* Initial public version.

Before this, the library was developed for personal use, starting with a few wrapper methods and expanding as needed.