# Telegram-bot (KtGram) changelog

## -7.0.0

* Reorganized internal structure (mostly interfaces and some types), imports may break.
* Renamed `chatData` > `classData` since it's more appropriate functionally.
* Decoupled `BotContext` (`userData`, `classData`) from bot itself, to set your own implementation there's new
  annotation `@CtxProvider`.
* Reworked base methods of `BotContext`, now they're more strongly typed.
* Added operator extensions `get`, `set` for `User` class that coupled with `userData`, i.e., now you
  can use `user["key"] = "value"`.
* Added experimental method `TelegramBot.getInstance` to get instance through defined `ClassManager`.
* Added `@InputChain` state manager that will save selected state from update automatically.
* Removed `EnvConfigLoader`, since it seems not much in demand.
* Added `catchExceptions` to the configuration to turn off catching exceptions to `caughtExceptions`.
* Added `throwExOnActionsFailure` to the configuration to throw exceptions on api request sending failures.
* Added function `retryOnTooManyRequests()` to `httpClient` configuration that returns `RetryStrategy` that retries on
  api request status `429` respecting its delay status.

### 6.6.0

* Covered 7.8 telegram api.

### 6.5.1

* Fixed `httpClient` wrong initialization exception in spring starter.

### 6.5.0

* Added `button(...)` function to define button in `answerInlineQuery` options.
* Added method `toWebhookResponse` to actions to give an option to respond to webhook.
* Added separate `bot.update.parse(...)`, helpful if you're using webhook responses.
* Fixed incorrect request url resolution when using multiple bot instances.
* Added ability to share `httpClient` between instances in ktor starter, spring starter will also use the httClient, if
  bean is defined.

### 6.4.1

* Fix `SuccessfulPayment` absence of `orderInfo` nullability bug.

## 6.4.0

* Change a request url forming scheme, now you can set protocol, plus added `isTestEnv` parameter to turn testing env.
* Added `bot.update.runExceptionHandler {}` to run ex. handling loop.
* Added secondary constructors for places where `ImplicitFile` used with `String`, `InputFile` options.
* Added `.sendReturning()` method (same as `sendAsync()`) for a more understandable flow.
* Moved `user_id` parameter from `send*` method to constructor
  in `createNewStickerSet`, `setStickerSetThumbnail`, `uploadStickerFile`, `addStickerToSet`,
  it wasn't obvious and didn't reflect the telegram api.
* Improved optional `thumbnail` passing for media (photo, document, etc.) actions.

### 6.3.1

* Fixed `webapp` onEvent handling.
* Changed `Update` usage in processes to `ProcessedUpdate` (changed also
  in `bot.handleUpdates()`, `bot.update.caughtExceptions`).
* Changed in `replyKeyboardMarkup {}` webApp right operand to url string.

## 6.3.0

* Covered 7.7 api.

## 6.2.0

* Covered 7.6 telegram api.
* Added `onInit` hook in spring-boot starter.
* Fixed a rare issue with sealed structures serialization.
* Changed parameter for source update in `ProcessedUpdate` from `update` to `origin`, old one is still remains but
  deprecated.

### 6.1.1

* Fix `String` always non-nullability issue in ksp processor.
* Fix `chatData` wrong clearing behavior, even within same class.
* Change `date` type from long to `Instant` in `RevenueWithdrawalState`, `StarTransaction`.
* Change `TransactionPartner.User` to `TransactionPartner.UserPartner`, to not confuse base `User` resolving.
* Added experimental `Collection<>.joinToInlineKeyboard` function to use basic pagination.

## 6.1.0

* Covered 7.5 Telegram api.

### 6.0.1

* Fix webapp `setParams` immutability.
* Fix ksp `UpdateType` resolving problem.
* Fix `photoHeight` missing in `InlineQueryResult`.

# 6.0.0

* Introduced new `@CommonHandler` which have nested annotations `@CommonHandler.Text`, `@CommonHandler.Regex`
  with rich configuration.
* Removed `@RegexCommandHandler` since it can be fully replaced with `@CommonHandler.Regex`.
* Changed guard interface from `Filter` to `Guard` since `@CommonHandler` have filtering mechanics.

## 5.5.0

* Added new guard mechanism to `@CommandHandler` (+`@CallbackQuery`), `@InputHandler` to make checks before processing.
* Added `beforeAction` and `afterAction` hooks to `ChainLink`. #153
* Added `ksp2` support, add `ksp.useKSP2=true` in gradle.properties to turn on.
* Fixed missing `chatId` in action `BusinessExt`.
* Fixed `TgUpdateHandler` logging level mismatching. #149
* Fixed `replyKeyboardMarkup` missing option changes. #152
* Covered Telegram `7.4` api version.

## 5.4.0

* Covered 7.3 telegram api.
* Added ability to use own implementation of `ChainLink` in `InputChain`.
* Improved Spring starter configuration.
* Removed old methods of `ListingBuilder` for multiple elements, now use `addAll()`.
* Added new builder for `sendPoll`/`poll` options.

### 5.3.2

* Fix `EntitiesContextualBuilder` bug.

### 5.3.1

* Fixed the wrong class manager passed to spring starter configuration.
* Added ability to manually configure instances.

## 5.3.0

* Fixed bug with throwing exception in ActionExt(Inline, Business).
* Added parameter to `@RegexCommandHandler` to pass regex options.
* Added experimental Spring starter.

## 5.2.0

* Added `ktor-starter` module for a quick webhook server setup.
* Added `identifier` parameter for `TelegramBot` instance.
* Changed `kotlinx-datetime` to transitive dependency, since its entities used in parameters.
* Returned a missing inline mode action extension for `getGameHighScores` method.
* Added `inputAutoRemoval` parameter to configuration, for more precise control of inputListener flow.
* Added missing 7.2 api `BiometricManager` to webapps.

## 5.1.0

* Fixed multipart requests data redundant quotation.
* Returned `logback` as logger for `jvm` target.
* Moved inline mode methods to extension interface from separate `InlinableAction`.
* Added `Any` upperbounds for `Autowiring` interface to avoid wrong behavior.
* Covered 7.2 TelegramApi changes.

### 5.0.5

* Fix nullable type resolving issue for `Autowiring` mechanics.
* Changed default ktor engine to java-http.

### 5.0.4

* Remove lazy structures that cause performance issues

### 5.0.3

* Fix `user` parameter missing in functional handlers.

### 5.0.2

* Fix ksp processor for regex handlers. #106
* Fix ksp processor InputHandler and RegexHandler RateLimits collecting.

### 5.0.1

* Changed internal modifier for `context {}` config section. #103
* Fixed annotation processing error in ksp processor for non-jvm modules.
* Added basic `ClassManager` for Kotlin Native target.

# 5.0.0

* Moved the library to kotlin multiplatform with `jvm`/`js`/`native` goals. Experimental at the moment.
* Serialization library replaced with `kotlinx-serialization`.
* Logging utility changed.
* Added webapp module.
* Moved message-related methods to a separate package (your imports could be broken).

### 4.3.1

* Fix coroutines space overconsumption bug.

## 4.3.0

* Changed naming of internal components.
    * `Action` (annotated entities) is now `Activity`, not to be confused with actions that form requests.
    * `ManualHandling` > `FunctionalHandling` - for better understanding.
* Added shortcut to `onFailure` method on `Deferred`, for consistency of approach.
* Added logging of the `Activity` list during the initialization phase.
* Internal structure of `FunctionalHandling` has been improved.
* Improved flow of coroutines in long polling mode, added request timeout in configuration, \
  see `updatesListener` block in configuration.
* Retry processing in the client has been improved, so it doesn't repeat all requests (e.g., 400 status),
  only the necessary ones.
* The type of the parameter `messageThreadId` has been changed to `Int`, as it better represents the data.
* The base scope of commands for friendliness towards beginners and better understanding has been
  changed to just `UpdateType.Message`. In other words, by default, it will have similar behavior to other types of
  libraries.

### 4.2.1

* Removed unnecessary `bot.addAutowiringObject` method.
* Enhanced `userData`/`chatData` get method with generic wrapped with catching for safety.
* Added `allowedUpdates` for long-polling since it necessary for getting special type updates.
* Fixed `giveawayMessageId` type: `Int` > `Long`.
* Added `replyParameters(messageId, block: ReplyParameters.() -> Unit)` fun-shortcut for changing replyParameters.
* Optimized action internal type inference.

## 4.2.0

* Added `send` method for `Chat` to `Action<>`.
* Changed ambiguous for `User`/`Chat`
  names: `MessageOrigin` (`User` > `UserOrigin`, `Chat` > `ChatOrigin`), `BotCommandScope.Chat` > `BotCommandScope.ChatScope`.
* Enhanced `linkPreviewOptions`: Introduced `disableWebPagePreview()` for easier disabling, and `linkPreviewOptions{}`
  for setting through lambda.
* Improved sealed class structure for better accessibility to general
  parameters (`MaybeInaccessibleMessage`, `MessageOrigin`, `ChatBoostSource`, `ChatMember`).
* Removed ReflectionHandler as CodegenHandler has proven effective.
* Changed long-polling handling, separated updates collecting and handling.
* Returned `coroutines` dependency as transitive, since it makes inconvenience in `sendAsync` methods process.
* Add `getOrNull()` method for `Deferred<Response<T>>` to lower verbosity for handling.
* Set default names for media methods using ByteArray and File to prevent incorrect API behavior when unset.
* Changed `botLogLevel` type to new proxy enum `LogLvl` since `logback` it's not transitive more.

## 4.1.0

* Added shortcut annotation for `CallbackQuery` handling > `@CommandHandler.CallbackQuery` (or you can import it and use
  without a prefix).
* Changed all date fields that represent timestamp to `Instant` and `Duration` when it's period.
* Changed structure of `caughtExceptions` event from `Pair` to `FailedUpdate`.
* Changed `logback`, `coroutines` from api-dependency to implementation-dependency (i.e., not transitive now).
* Covered Telegram API `7.0` version.

### 4.0.1

* Fix parsing annotations bug when parameters are not well-ordered.

# 4.0.0

* Added the ability to collect actions in compile time.
* Improved support of input chains in annotation mode.
* Added support for all scopes for commands.
* Added ability to pass all subclasses of `ProcessedUpdate` in new codegen update handler.
* Improved signature of `copyMessage`, `forwardMessage`, `banChatSenderChat`, `unbanChatSenderChat` methods.

## 3.5.0

* Added infix functions to `EntitiesBuilder` (which is used in features by `caption {}` and `entities {}`).
* Added infix function to the builder used in `setMyCommands` for ease of adding a command.
* Changed the function for methods `answerInlineQuery`, `answerShippingQuery`, `poll`, `setPassportDataErrors` that
  used `array`/`list` to collect elements, added a new `ListingBuilder`, for convenience of adding enumerable elements.
* Wherever `userId` is used in function-methods, shortcuts using `User` have been added.
* For those methods where the `send*` keyword in the function name was omitted, the method shortcuts are added.
* Refactored the internal structure of actions and features.

## 3.4.0

* Added `InputChaining` in annotation mode as experimental feature.
* Added `additionalHeaders` that will be applied to requests to `HttpConfiguration`
    * (maybe useful for authorization via socks proxy)
* Added `Deffered<Response<T>>.foldResponse()` function to handle async responses.

### 3.3.1

* Added ability to use proxy. See `httpClient{}` section in bot configuration.

## 3.3.0

* Fixed bug with missing media within an input handling process.
* Moved rate limit mechanism setting and its general limits to one configuration section `rateLimiter{}`, also now you
  can set rate exceeded action in configuration.
* Added new convenient shortcuts for `InputListener` and `BotContext` interfaces.
* Little internal code optimizations + lib versions bumps.

## 3.2.0

* Added the fields `fromRequest` and `fromAttachmentMenu` to the class `WriteAccessAllowed`.
* Added the new administrator privileges `canPostStories`, `canEditStories` and `canDeleteStories` to the
  classes `ChatMember.Administrator` and `ChatAdministratorRights`.
* Added the parameters `canPostStories`, `canEditStories` and `canDeleteStories` to the method `promoteChatMember()`.

## 3.1.0

* Changed `Autowiring` and `ClassManager` interfaces to fun interfaces.
* Added extensions to `ProcessedUpdate` class.
    * parameter `userOrNull` which returns `User` or `null` :)
    * function `getUser` which returns `User` or throws NPE.
      (it's actually nullable only in rare cases, but anyway, use it
      with caution)
* Added shortcut functions to `Markup` interfaces for `InlineKeyboardMarkup`, `ReplyKeyboardMarkup`, `ForceReply`.

### 3.0.4

* Improved signatures of some sticker-set and chat related methods.

### 3.0.3

* Reworked multipart actions logic and fixed related bugs.

### 3.0.2

* Make manual handling flow similar to annotation regarding regex processing.

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
* Add additional parsing for a deeplink case.

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

* Add a new method for defining behavior of update processing.
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
* Created new convenient DSL for creating markup - `replyKeyboardMarkup()`, similar to an inline keyboard version, see
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
* Added handling of commands containing at character, i.e., such as `/command@bot`
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
  methods and not to overload the methods that do not have an inline mode.
* Make ManualHandlingBehaviour parameter lazy, is made to optimize cases where only annotation processing is used.
* Moved option saving mechanism from helper functions to mapper, for more consistency.
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