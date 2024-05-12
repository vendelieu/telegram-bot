![Telegram bot api library logo](https://user-images.githubusercontent.com/3987067/180802441-966bb058-919b-4e1c-82c1-2b210cc9a94e.png)

# Telegram Bot

[![Maven Central](https://img.shields.io/maven-central/v/eu.vendeli/telegram-bot?style=flat&label=Maven&logo=apache-maven)](https://search.maven.org/artifact/eu.vendeli/telegram-bot)
[![Supported version](https://img.shields.io/badge/Bot%20Api-7.3-blue?logo=telegram)](https://core.telegram.org/bots/api#may-6-2024)\
[![KDocs](https://img.shields.io/static/v1?label=Dokka&message=KDocs&color=blue&logo=kotlin)](https://vendelieu.github.io/telegram-bot/)
[![codecov](https://codecov.io/gh/vendelieu/telegram-bot/branch/master/graph/badge.svg?token=xn5xo6fu6r)](https://codecov.io/gh/vendelieu/telegram-bot) \
[![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Chat&color=blue&logo=telegram)](https://t.me/venny_tgbot)
[![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Channel&color=blue&logo=telegram)](https://t.me/v_telegramBot)

Telegram Bot Api wrapper with a user-friendly interface.

# Installation

Add the ksp plugin and library to the dependencies.

build.gradle.kts example:

```gradle
plugins {
    // ...
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
}

dependencies {
    // ...
    implementation("eu.vendeli:telegram-bot:5.4.0")
    ksp("eu.vendeli:ksp:5.4.0")
}
```

Or if you use a different builder, look in
the [installation](https://github.com/vendelieu/telegram-bot/wiki/Installation) article.

# Samples

- [Template repository](https://github.com/vendelieu/telegram-bot_template) - draft example.
- [FeedbackBot](https://github.com/ktgram/feedback-bot) - Use ready example of a feedback bot.
- [Conversation](https://github.com/vendelieu/telegram-bot_template/tree/conversation) - An example of using `FSM` and
  usage of `BotContext`.
- [Echo](https://github.com/vendelieu/telegram-bot_template/tree/echo) - Echo bot :)
- [Poll](https://github.com/vendelieu/telegram-bot_template/tree/poll) - An example of how to build a questionnaire bot.
- [Ktor webhook starter](https://github.com/ktgram/webhook) - An example of using webhook mode
    with Ktor.
- [Spring Boot usage](https://github.com/vendelieu/telegram-bot_template/tree/spring-bot) - An example of using the bot
  organically in the Spring ecosystem, using its built-in DI.
- [Heroku ready example](https://github.com/vendelieu/telegram-bot_template/tree/heroku) - An example of a bot working
  via Heroku.

<details>
  <summary>More samples</summary>

- [Native example](https://github.com/ktgram/native-example) - An example of using a bot with Kotlin Native target.
- [Web app](https://github.com/ktgram/webapp) - Example of a bot using Telegram Webapps.

</details>

# Usage

```kotlin
suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN")

    bot.handleUpdates()
    // start long-polling listener
}

@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello, what's your name?" }.send(user, bot)
    bot.inputListener[user] = "conversation"
}

@RegexCommandHandler("blue colo?r")
suspend fun color(user: User, bot: TelegramBot) {
    message { "Oh you also like blue colour?" }.send(user, bot)
}

@InputHandler(["conversation"])
suspend fun startConversation(user: User, bot: TelegramBot) {
    message { "Nice to meet you, ${message.text}" }.send(user, bot)
    message { "What is your favorite food?" }.send(user, bot)
    bot.inputListener.set(user) { "conversation-2step" } // another way to set input
}
//..
```

It is also possible to process updates functionally:

```kotlin
fun main() = runBlocking {
    val bot = TelegramBot("BOT_TOKEN")

    bot.handleUpdates { update ->
        onCommand("/start") {
            message { "Hello, what's your name?" }.send(user, bot)
            bot.inputListener[user] = "conversation"
        }
        inputChain("conversation") {
            message { "Nice to meet you, ${message.text}" }.send(update.getUser(), bot)
            message { "What is your favorite food?" }.send(update.getUser(), bot)
        }.breakIf({ message.text == "peanut butter" }) { // chain break condition
            message { "Oh, too bad, I'm allergic to it." }.send(update.getUser(), bot)
            // action that will be applied when match
        }.andThen {
            // next input point if break condition doesn't match
        }
    }
}
```

### Configuration

The library has very flexible customization options, \
and the ability to configure from the environment variables out of the box is also provided.

You can read more in a [Bot configuration](https://github.com/vendelieu/telegram-bot/wiki/Bot-configuration) article.

### Processing responses

if you want to operate with response you can
use [`sendAsync()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.interfaces/-action/send-async.html)
instead
of [`send()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.interfaces/-action/send.html)
method, which
returns [`Response`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-response/index.html):

```kotlin
message { "test" }.sendAsync(user, bot).onFailure {
    println("code: ${it.errorCode} description: ${it.description}")
}
```

Any async request returns
a [`Response`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-response/index.html)
on which you can also use
methods [`getOrNull()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/get-or-null.html)
, [`isSuccess()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/is-success.html)
, [`onFailure()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/on-failure.html)

### Additional resources

> There is a [wiki](https://github.com/vendelieu/telegram-bot/wiki) section that have helpful information.

### Questions

You're always welcome in our [chat](https://t.me/venny_tgbot), feel free to ask.

## Acknowledgements

A big thank you to everyone who has contributed to this project. Your support and feedback are invaluable.

If you find this library useful, please consider giving it a star. Your support helps us continue to improve
and maintain this project.