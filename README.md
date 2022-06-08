# Kotlin Telegram Bot [![](https://jitpack.io/v/vendelieu/telegram-bot.svg)](https://jitpack.io/#vendelieu/telegram-bot) [![Supported version](https://img.shields.io/badge/Telegram%20Bot%20API-6.0-blue)](https://core.telegram.org/bots/api-changelog#april-16-2022)

[![KDocs](https://img.shields.io/static/v1?label=Dokka&message=KDocs&color=blue&logo=kotlin)](https://vendelieu.github.io/telegram-bot/)

Kotlin based wrapper over Telegram API.

# Principles

### Annotations Magic.

The basic interaction with the library is done on the basis of annotations, which simplifies working with commands and
gives the possibility to separate by context.\
It is very similar to the Spring RestController.

The main goal was to reduce the time spent on commands routing.

### Less time to the bot interface - More time to the business logic behind it.

*Create application - Put annotation on - Write business logic.*\
This keeps development to a minimum of interactions with the library interface, and allows you to focus on the business
logic.

### The interface should be lightweight.

The library interface is intentionally not complicated by all sorts of filters, steps, exception handling and other all
sorts of features to not complicate the basic processing as the library API already gives the opportunity to build the
necessary actions yourself.

# Installation

Add the JitPack repository to your root build.gradle.kts file:

```gradle
repositories {
    maven("https://jitpack.io")
}
```

Now add the library itself to the dependencies' module that you need it.

```gradle
dependencies {
    implementation("com.github.vendelieu:telegram-bot:1.2.2")
}
```

# Samples

You can see the samples in the [samples' folder](https://github.com/vendelieu/telegram-bot/tree/master/samples)
there you can find:

- Conversation - An example of using Inputs and storing data in UserData.
- Echo - Echo bot :)
- Exception-handling - Simple example of exception handling
- Ktor-webhook - An example of using webhook with Ktor
- Poll - An example of how to build a bot questionnaire.

# Usage

```kotlin
suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.example.controllers")
    /**
     * Second parameter is the package in which commands/inputs will be searched.
     */

    bot.update.setListener { // set long-polling listener and use defined action over updates. 
        bot.update.handle(it)
    }
}

@TelegramCommand(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello" }.send(to = user, via = bot)
}
```

for webhook handling you can use any server and use `bot.update.handle`
and for set webhook you can use this method:

```kotlin
setWebhook("https://site.com").send(bot)
```

also it is possible directly instantiate actions

```kotlin
SendMessageAction("Hello").send(to = user, via = bot)
```

and there's some methods applicable depending on the action:

```kotlin
message { "Hello *user*! \$hello" }.options { parseMode = ParseMode.Markdown }.markup {
    // this method takes all Keyboard types: ReplyKeyboardMarkup, InlineKeyboardMarkup, ForceReply, ReplyKeyboardRemove.
    inlineKeyboardMarkup {
        "testButton" callback "callbackPoint"
    } // this is InlineKeyboardMarkup builder interface, but you can directly create your own markup.
    /**
     * InlineKeyboardMarkup(InlineKeyboardButton("test", callbackData = "testCallback"))
     */
}.entities {
    entity(EntityType.Cashtag, 14, 5)
}.send(user, bot)
```

if you want to operate with response you can use `sendAsync()` instead of `send()` method, which returns `Response`:

```kotlin
message { "test" }.sendAsync(user, bot).await().onFailure {
    println("code: ${it.errorCode} description: ${it.description}")
}
```

Any async request returns a `Response` on which you can also use methods `getOrNull()` or `isSuccess()`.

### Input waiting

The library also provides an input waiting interface that can be used for multistep logic:

```kotlin
@TelegramCommand(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello, please enter your name:" }.send(to = user, via = bot)
    bot.input.set(user.id, "nameInputCatch")
}

@TelegramInput(["nameInputCatch"])
suspend fun nameInputCatch(update: ProcessedUpdate, bot: TelegramBot) {
    message { "My name is Adam, nice to meet you ${update.text}!" }.send(update.user, bot)
}
```

The basic implementation of waiting inputs uses `synchronized(WeakHashMap<>)`\
but for custom use we still recommend you to use your own implementation of storage based on your preferred tools
(redis/caffeine/etc.).\
All you have to do is implement `BotWaitingInput` interface.

### Handling unhandled :)

It is also possible to process updates that have not been processed by any command or input.

```kotlin
@TelegramUnhandled
suspend fun unhandledUpdates(user: User, bot: TelegramBot) {
    message { "The bot does not understand what you want him to do." }.send(user, bot)
}
```

### Callback processing

With the callback you can pass data in the format query parameters, \
e.g. `greeting?name=Adam` in this example greeting is a command and everything after ? is data\
in the format of key=value with `&` separator.

you must use the parameter name in the target function or use the `@TelegramParameter` annotation to pass the data
correctly.

The correct target function for our command `greeting?name=Adam` would be this example:

```kotlin
@TelegramCommand(["greeting"])
suspend fun greeting(name: String, user: User, bot: TelegramBot) {
    message { "Hello, $name" }.send(to = user, via = bot)
}
```

or with `@TelegramParameter` annotation:

```kotlin
@TelegramCommand(["greeting"])
suspend fun greeting(@TelegramParameter("name") anyParameterName: String, user: User, bot: TelegramBot) {
    message { "Hello, $anyParameterName" }.send(to = user, via = bot)
}
```

The use of `@TelegramParameter` annotation is also very helpful\
when you need to send a large amount of data because the Telegram API has a limit of 64 characters in the callback.

So you can abbreviate the variable names in the callback itself and use clear readable names in the code.

#### Supported value types

Values passed through the callback can only be of certain java base types as:

- String
- Integer
- Long
- Short
- Float
- Double

If a parameter was declared as a certain type, but the passed value does not match, then `null` will be passed.

### Magical objects

After explaining the magic of transferring data through the callback, a reasonable question may arise,\
what are the objects User, TelegramBot, ProcessedUpdate and how do they get to the target function\
if they are not specified anywhere in the callback, in the command or in the input?

The point is that these are internal library magic objects that are automatically passed to the target function if they
have been declared.\
That is, if you need them, you can declare, and they will be transferred, if not, and there will be no problem.

- `ProcessedUpdate` - current update processed by bot.
- `User` - user object, for those cases when we do not need to use all update, but only user to send him a message.
- And finally `TelegramBot`, this is the instance of the current bot required for sending actions.

It is also possible to add your own magic objects with `MagicObject` interface:

```kotlin
data class UserContext(val id: Long)

bot.addMagicObject(UserContext::class.java) {
    object : MagicObject<UserContext> {
        //object will be dynamically generated by this function each time
        override fun get(update: ProcessedUpdate, bot: TelegramBot): UserContext {
            // You can, for example, take the internal user ID from the database by telegramId
            // db.select().from(User).where(telegram_id = update.user.id)
            return UserContext(1)
        }
    }
}
```

### Inline buttons builder

Also, for inline buttons there is a handy markup build interface.

```kotlin
inlineKeyboardMarkup {
    "name" callback "callbackData"         //---\
    "buttonName" url "https://google.com"  //--- these two buttons will be in the same row.
    newLine() // or br()
    "otherButton" webAppInfo "data"       // this will be in other row

    // you can also use a different style within the builder:
    callbackData("buttonName") { "callbackData" }
}
```

but it also does not prevent you from manually building a markup as seen in the initial example:

```kotlin
InlineKeyboardMarkup(
    InlineKeyboardButton("test", callbackData = "testCallback")
)
```

### Bot Context

The bot can also provide the ability to remember some data through the userData and chatData interfaces.

- userData is a user-level cache.
- chatData is a class-level cache, i.e. the cache will be stored until the user moves to a command or input that is in a
  different class. (userData must be present for this scheme to work correctly)

These tools are not provided by default, but can be implemented through `BotUserData` and `BotChatData` interfaces using
the data storage tools of your choice.

To install, all you need to do is assign via TelegramBot:

```kotlin
suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.example.controllers")
    bot.userData = BotUserDataImpl()
    bot.chatData = BotChatDataImpl()

    bot.update.setListener {
        bot.update.handle(it)
    }
}

@TelegramCommand(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    bot.userData?.set(user.id, "test", "value") // now we can use it in any part of our code
    message { "Hello" }.send(to = user, via = bot)
}

@TelegramCommand(["/test"])
suspend fun test(user: User, bot: TelegramBot) {
    val testVal = bot.userData?.get(user.id, "test").toString()
    message { "stored value: $testVal" }.send(to = user, via = bot)
}
```

### DI

Also, if you want to use some libraries for Dependency Injection you can redefine the `ClassManager` interface using
your
preferred mechanism and pass it on when initializing the bot.

```kotlin
suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.example.controllers", classManager = ClassManagerImpl())

    bot.update.setListener {
        bot.update.handle(it)
    }
}
```