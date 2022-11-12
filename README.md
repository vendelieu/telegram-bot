![Telegram bot api library logo](https://user-images.githubusercontent.com/3987067/180802441-966bb058-919b-4e1c-82c1-2b210cc9a94e.png)

# Telegram Bot

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.vendeli/telegram-bot/badge.svg)](https://maven-badges.herokuapp.com/maven-central/eu.vendeli/telegram-bot)
[![Supported version](https://img.shields.io/badge/Bot%20API-6.3-blue)](https://core.telegram.org/bots/api-changelog#november-5-2022)

[![KDocs](https://img.shields.io/static/v1?label=Dokka&message=KDocs&color=blue&logo=kotlin)](https://vendelieu.github.io/telegram-bot/)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin) [![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Chat&color=blue&logo=telegram)](https://t.me/vennyTgBotAPI)

Kotlin based wrapper over Telegram API.

# Installation

Add the MavenCentral repository to your root build.gradle.kts file:

```gradle
repositories {
    mavenCentral()
}
```

Now add the library itself to the dependencies' module that you need it.

```gradle
dependencies {
    implementation("eu.vendeli:telegram-bot:2.3.2")
}
```

# Samples

You can see the samples in the [telegram-bot_template](https://github.com/vendelieu/telegram-bot_template) repository.
In the basic branch itself there is an empty draft that can be used to create any bot you want.

there you can find in the appropriate branches:

- [Conversation](https://github.com/vendelieu/telegram-bot_template/tree/conversation) - An example of using Inputs and
  storing data in UserData.
- [Echo](https://github.com/vendelieu/telegram-bot_template/tree/echo) - Echo bot :)
- [Exception-handling](https://github.com/vendelieu/telegram-bot_template/tree/exception-handling) - Simple example of
  exception handling
- [Ktor-webhook](https://github.com/vendelieu/telegram-bot_template/tree/ktor-webhook) - An example of using webhook
  with Ktor
- [Poll](https://github.com/vendelieu/telegram-bot_template/tree/poll) - An example of how to build a bot questionnaire.
- [Spring Boot usage](https://github.com/vendelieu/telegram-bot_template/tree/spring-bot) - An example of using the bot
  organically in the Spring ecosystem, using its built-in DI.
- [Heroku ready example](https://github.com/vendelieu/telegram-bot_template/tree/heroku) - An example of a bot working
  via Heroku

# Usage

```kotlin
fun main() = runBlocking {
    val bot = TelegramBot("BOT_TOKEN", "com.example.controllers")
    /**
     * Second parameter is the package in which commands/inputs will be searched.
     */

    bot.handleUpdates()
    // start long-polling listener
}

@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello, what's your name?" }.send(user, bot)
    bot.inputListener.set(user.id, "conversation")
}

@InputHandler(["conversation"])
suspend fun startConversation(user: User, bot: TelegramBot) {
    message { "Nice to meet you, ${message.text}" }.send(user, bot)
    message { "What is your favorite food?" }.send(user, bot)
    bot.inputListener.set(user.id, "conversation-2step")
}
//..
```

It is also possible to process updates manually:

```kotlin
fun main() = runBlocking {
    val bot = TelegramBot("BOT_TOKEN")

    bot.handleUpdates { update ->
        onCommand("/start") {
            message { "Hello, what's your name?" }.send(user, bot)
            bot.inputListener.set(user.id, "conversation")
        }
        inputChain("conversation") {
            message { "Nice to meet you, ${message.text}" }.send(user, bot)
            message { "What is your favorite food?" }.send(user, bot)
        }.breakIf({ message.text == "peanut butter" }) { // chain break condition
            message { "Oh, too bad, I'm allergic to it." }.send(user, bot)
            // action that will be applied when match
        }.andThen {
            // ...
        }
    }
}
```

You can also change additional parameters of the bot:

```kotlin
// ...
val bot = TelegramBot("BOT_TOKEN") {
    inputListener = RedisInputListenerImpl()
    classManager = KoinClassManagerImpl()
    logging {
        botLogLevel = Level.DEBUG
    }
}
// ...
```

A more complete list of settings can be found
in [BotConfiguration](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-bot-configuration/index.html)
class.

It is also possible to do more advanced processing with a manual listener setting
with [`bot.update.setListener {}`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.core/-telegram-update-handler/set-listener.html)
function.

for webhook handling you can use any server
and [`bot.update.handle()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.core/-telegram-update-handler/handle.html)
function (or use this function if you're directly setting listener), \
and for set webhook you can use this method:

```kotlin
setWebhook("https://site.com").send(bot)
```

if you want to operate with response you can
use [`sendAsync()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.interfaces/send-async.html)
instead of `send()` method, which
returns [`Response`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-response/index.html):

```kotlin
message { "test" }.sendAsync(user, bot).await().onFailure {
    println("code: ${it.errorCode} description: ${it.description}")
}
```

Any async request returns
a [`Response`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-response/index.html)
on which you can also use
methods [`getOrNull()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/get-or-null.html)
, [`isSuccess()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/is-success.html)
, [`onFailure()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/on-failure.html)
.

# More about

You can also read more information in the [wiki](https://github.com/vendelieu/telegram-bot/wiki), and you're always
welcome in [chat](https://t.me/vennyTgBot).
