![Telegram bot api library logo](https://user-images.githubusercontent.com/3987067/180802441-966bb058-919b-4e1c-82c1-2b210cc9a94e.png)

# Kotlin Telegram Bot
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.vendeli/telegram-bot/badge.svg)](https://maven-badges.herokuapp.com/maven-central/eu.vendeli/telegram-bot)
[![Supported version](https://img.shields.io/badge/Bot%20API-6.2-blue)](https://core.telegram.org/bots/api-changelog#august-12-2022)

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
    implementation("eu.vendeli:telegram-bot:2.1.0")
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

    bot.handleUpdates()
    // start long-polling listener
}

@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello" }.send(to = user, via = bot)
}
```

It is also possible to process manually:

```kotlin
suspend fun main() {
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
welcome in [chat](https://t.me/vennyTgBotAPI).
