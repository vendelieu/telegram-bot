![logo](https://user-images.githubusercontent.com/3987067/177022538-6dd410e2-ec65-42d6-8d5f-0f8e4a0de577.png)

# Kotlin Telegram Bot [![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.vendeli/telegram-bot/badge.svg)](https://maven-badges.herokuapp.com/maven-central/eu.vendeli/telegram-bot) [![Supported version](https://img.shields.io/badge/Telegram%20Bot%20API-6.1-blue)](https://core.telegram.org/bots/api-changelog#june-20-2022)

[![KDocs](https://img.shields.io/static/v1?label=Dokka&message=KDocs&color=blue&logo=kotlin)](https://vendelieu.github.io/telegram-bot/)
[![Github Wiki](https://img.shields.io/badge/Github-Wiki-green)](https://github.com/vendelieu/telegram-bot/wiki)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

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
    implementation("eu.vendeli:telegram-bot:1.5.0")
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

@TelegramCommand(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Hello" }.send(to = user, via = bot)
}
```

It is also possible to process manually:

```kotlin
suspend fun main() {
    val bot = TelegramBot("BOT_TOKEN", "com.example.controllers")

    bot.handleUpdates { update ->
        onCommand("/start") {
            message { "Hello" }.send(update.message!!.from!!.id, bot)
        }
    }
}
```

It is also possible to process in more detail with a manual listener setting
with [bot.update.setListener](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.core/-telegram-update-handler/set-listener.html)
{} function.

for webhook handling you can use any server
and [`bot.update.handle()`](https://vendelieu.github.io/telegram-bot/-telegram%20-bot/eu.vendeli.tgbot.core/-telegram-update-handler/handle.html)
function, \
and for set webhook you can use this method:

```kotlin
setWebhook("https://site.com").send(bot)
```

also it is possible directly instantiate actions

```kotlin
SendMessageAction("Hello").send(to = user, via = bot)
```

if you want to operate with response you can use `sendAsync()` instead of `send()` method, which returns `Response`:

```kotlin
message { "test" }.sendAsync(user, bot).await().onFailure {
    println("code: ${it.errorCode} description: ${it.description}")
}
```

Any async request returns a `Response` on which you can also use methods `getOrNull()` or `isSuccess()`.

# More about

You can also read more information in the [wiki](https://github.com/vendelieu/telegram-bot/wiki).
