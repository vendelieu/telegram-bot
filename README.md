![Telegram bot api library logo](https://user-images.githubusercontent.com/3987067/180802441-966bb058-919b-4e1c-82c1-2b210cc9a94e.png)

# Telegram Bot

[![Maven Central](https://img.shields.io/maven-central/v/eu.vendeli/telegram-bot?style=flat&label=Maven&logo=apache-maven)](https://search.maven.org/artifact/eu.vendeli/telegram-bot)
[![Supported version](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fgithub.com%2Fvendelieu%2Ftelegram-bot%2Fraw%2Fmaster%2FbuildSrc%2Fsrc%2Fmain%2Fresources%2Fapi.json&query=%24.version&style=flat&logo=telegram&label=Telegram&color=blue)](https://core.telegram.org/bots/api)\
[![KDocs](https://img.shields.io/static/v1?label=Dokka&message=KDocs&color=blue&logo=kotlin)](https://vendelieu.github.io/telegram-bot/)
[![codecov](https://codecov.io/gh/vendelieu/telegram-bot/branch/master/graph/badge.svg?token=xn5xo6fu6r)](https://codecov.io/gh/vendelieu/telegram-bot) \
[![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Chat&color=blue&logo=telegram)](https://t.me/venny_tgbot)
[![Chat in Telegram](https://img.shields.io/static/v1?label=Telegram&message=Channel&color=blue&logo=telegram)](https://t.me/kotlingram)

Telegram Bot Api wrapper with a user-friendly interface.

# Installation

Add the ksp plugin and library plugin to your Gradle build file.

build.gradle.kts example:

```gradle
plugins {
    // ...
    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
    id("eu.vendeli.telegram-bot") version "7.2.2"
}
```

<details>
<summary>Manually</summary>
To set up the project without using the plugin, you need to add a dependency and configure the ksp processor:

```gradle
plugins {
    // ...
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
}

dependencies {
    // ...
    implementation("eu.vendeli:telegram-bot:7.2.2")
    ksp("eu.vendeli:ksp:7.2.2")
}
```

For multiplatform, you need to add the dependency to common sources and define ksp for the targets you need, see example
in [native-example](https://github.com/ktgram/native-example/blob/master/build.gradle.kts).
</details>


<details>
  <summary><i>Snapshots</i></summary>

[![Snapshot version](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fv229149.hosted-by-vdsina.ru%2Fsnap-ver%2Ftelegram-bot&query=%24%5B0%5D.name&logo=github&label=SNAPSHOT&link=https%3A%2F%2Fgithub.com%2Fvendelieu%3Ftab%3Dpackages%26repo_name%3Dtelegram-bot)](https://github.com/vendelieu?tab=packages&repo_name=telegram-bot)

To install snapshot versions, add snapshot repository,
if you're using plugin just use `addSnapshotRepo` parameter:

```gradle
ktGram {
    forceVersion = "branch-xxxxxx~xxxxxx"
    addSnapshotRepo = true
}
```

or manually add repository:

```gradle
repositories {
    mavenCentral()
    // ...
    maven("https://mvn.vendeli.eu/telegram-bot") // this
}
```

And add library dependency (with ksp processor) as described in `manually` section using the latest package version
from [packages](https://github.com/vendelieu?tab=packages&repo_name=telegram-bot) or from badge above.

</details>

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

@InputHandler(["conversation"], guard = UserPresentGuard::class)
suspend fun startConversation(update: ProcessedUpdate, user: User, bot: TelegramBot) {
    message { "Nice to meet you, ${update.text}" }.send(user, bot)
    message { "What is your favorite food?" }.send(user, bot)
    bot.inputListener.set(user) { "conversation-2step" } // another way to set input
}

@CommonHandler.Regex("blue colo?r")
suspend fun color(user: User, bot: TelegramBot) {
    message { "Oh you also like blue color?" }.send(user, bot)
}
//..
```

*a little more detailed about handlers you can see
in [handlers](https://github.com/vendelieu/telegram-bot/wiki/Handlers) article.*

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

The library has very flexible customization options, and there are different options to configure through external sources.

You can read more in a [Bot configuration](https://github.com/vendelieu/telegram-bot/wiki/Bot-configuration) article.

### Processing responses

To process over response or/and have more control over request flow
use [
`sendReturning()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.interfaces.action/-action/send-returning.html)
instead
of [
`send()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.interfaces.action/-action/send.html)
method,
which
returns [
`Response`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-response/index.html):

```kotlin
message { "test" }.sendReturning(user, bot).onFailure {
    println("code: ${it.errorCode} description: ${it.description}")
}
```

Any `sendReturning` method returns
a [
`Response`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/-response/index.html)
on which you can also use
methods [
`getOrNull()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/get-or-null.html)
, [
`isSuccess()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/is-success.html)
, [
`onFailure()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.types.internal/on-failure.html)

### Additional resources

> There is a [wiki](https://github.com/vendelieu/telegram-bot/wiki) section that have helpful information.

### Questions

You're always welcome in our [chat](https://t.me/venny_tgbot), feel free to ask.

## Acknowledgements

A big thank you to everyone who has contributed to this project. Your support and feedback are invaluable.

If you find this library useful, please consider giving it a star. Your support helps us continue to improve
and maintain this project.
