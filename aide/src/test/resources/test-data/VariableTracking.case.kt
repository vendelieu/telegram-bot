// PROCESSOR: ErrorOnlyProcessor
// EXPECTED:
// w: file:///VariableCases.kt:14:13 Missing send call
// w: file:///VariableCases.kt:21:13 Missing send call
// END

// FILE: VariableCases.kt

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message

@CommandHandler(["/start"])
suspend fun variableCases(user: User, bot: TelegramBot) {
    // Valid chain
    message { "A" }.apply {
        send(user, bot)
    }

    // Invalid - reassigned but not sent
    val b = message { "B" }
    val c = b
    c.toString() // No send

    // Partial send
    val d = message { "D1" }
    d.send(user, bot)
    val e = message { "D2" } // Not sent

    // Nested scopes
    message { "E" }.run {
        message { "F" }.also {
            it.send(user, bot)
        }
        // E not sent
    }
}
