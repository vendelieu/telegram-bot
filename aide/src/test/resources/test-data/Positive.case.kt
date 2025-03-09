// PROCESSOR: ErrorOnlyProcessor
// EXPECTED:
// END

// FILE: ValidCases.kt

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message

@CommandHandler(["/start"])
suspend fun validCases(user: User, bot: TelegramBot) {
    // Direct call with send
    message { "Hello" }.send(user, bot)

    // Variable assignment with send
    val a = message { "Test" }
    a.send(user, bot)

    // Scope function with send
    message { "Scope" }.run {
        send(user, bot)
    }

    // Chained calls
    message { "Chain" }
        .apply {
            print(this)
        }.send(user, bot)
}
