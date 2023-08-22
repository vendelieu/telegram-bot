![Telegram bot api library logo](https://user-images.githubusercontent.com/3987067/180802441-966bb058-919b-4e1c-82c1-2b210cc9a94e.png)

# Telegram Bot

[![Maven Central](https://img.shields.io/maven-central/v/eu.vendeli/telegram-bot?style=flat&label=Maven&logo=apache-maven)](https://search.maven.org/artifact/eu.vendeli/telegram-bot)
[![Supported version](https://img.shields.io/badge/Bot%20Api-6.8-blue?logo=telegram)](https://core.telegram.org/bots/api#august-18-2023)

[![KDocs](https://img.shields.io/static/v1?label=Dokka&message=KDocs&color=blue&logo=kotlin)](https://vendelieu.github.io/telegram-bot/)
[![codecov](https://codecov.io/gh/vendelieu/telegram-bot/branch/master/graph/badge.svg?token=xn5xo6fu6r)](https://codecov.io/gh/vendelieu/telegram-bot)

[![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Chat&color=blue&logo=telegram)](https://t.me/vennyTgBot)
[![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Channel&color=blue&logo=telegram)](https://t.me/v_telegramBot)

Telegram Bot Api wrapper with a user-friendly interface.

# Installation

Add the library itself to the dependencies' module that you need it.

build.gradle.kts example:

```gradle
dependencies {
    implementation("eu.vendeli:telegram-bot:3.0.3")
}
```

Or if you use a different builder, look in
the [installation](https://github.com/vendelieu/telegram-bot/wiki/Installation) article.

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
- [Poll](https://github.com/vendelieu/telegram-bot_template/tree/poll) - An example of how to build a questionaire bot.
- [Spring Boot usage](https://github.com/vendelieu/telegram-bot_template/tree/spring-bot) - An example of using the bot
  organically in the Spring ecosystem, using its built-in DI.
- [Heroku ready example](https://github.com/vendelieu/telegram-bot_template/tree/heroku) - An example of a bot working
  via Heroku

# Usage

```kotlin
suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.example.controllers")
    /**
     * Second parameter is the package in which commands/inputs will be searched.
     */

    bot.handleUpdates()
    // start long-polling listener
}
// in your controller:
@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello, what's your name?" }.send(user, bot)
    bot.inputListener.set(user.id, "conversation")
}

@RegexCommandHandler("blue colo?r")
suspend fun color(user: User, bot: TelegramBot) {
  message { "Oh you also like blue colour?" }.send(user, bot)
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
            // next input point if break condition doesn't match
        }
    }
}
```

There is also a [wiki](https://github.com/vendelieu/telegram-bot/wiki) section which is updated frequently.

You are welcome to check it out :)

### Configuration

The library has very flexible customization options, \
and the ability to confgure from the environment variables out of the box is also provided.

You can read more in a [Bot configuration](https://github.com/vendelieu/telegram-bot/wiki/Bot-configuration) article.

### Requests limiting

Library as well supports limiting requests from users:

```kotlin
// ...
val bot = TelegramBot("BOT_TOKEN") {
    rateLimits { // general limits
        period = ...
        rate = ...
    }
}

// Limits on certain actions
@CommandHandler(["/start"], RateLimits(period = 1000L, rate = 1L)) // or InputHandler
suspend fun start(user: User, bot: TelegramBot) {
    // ...
}
// In manual mode
onCommand("/start", RateLimits(period = 1000L, rate = 1L)) {
    // ...
}
// ...
```

### Processing responses

if you want to operate with response you can
use [`sendAsync()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.interfaces/-action/send-async.html)
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

### Questions

You're always welcome in our [chat](https://t.me/vennyTgBot), feel free to ask.
