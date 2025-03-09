// PROCESSOR: ErrorOnlyProcessor
// EXPECTED:
// w: file:///MissingSendCases.kt:14:13 Missing send call
// w: file:///MissingSendCases.kt:17:5 Missing send call
// END

// FILE: MissingSendCases.kt

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message

fun random() = true

@CommandHandler(["/start"])
suspend fun invalidCases(user: User, bot: TelegramBot) {
    // Direct action without send
    message { "Direct" }

    // Variable assignment without send
    val a = message { "Var" }

    // Scope function without send
    message { "Scope" }.run {
        // No send here
    }

    // Conditional send
    if (random()) {
        message { "Conditional" }
    }
}
