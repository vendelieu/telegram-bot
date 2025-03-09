// PROCESSOR: ErrorOnlyProcessor
// EXPECTED:
// w: file:///ScopeCases.kt:14:5 Missing send call
// w: file:///ScopeCases.kt:24:5 Missing send call
// w: file:///ScopeCases.kt:29:5 Missing send call
// END

// FILE: ScopeCases.kt

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message

@CommandHandler(["/start"])
suspend fun scopeCases(user: User, bot: TelegramBot) {
    // Valid let scope
    message { "Let" }.let {
        it.send(user, bot)
    }

    // Invalid apply
    message { "Apply" }.apply {
        // No send
    }

    // Valid run
    message { "Run" }.run {
        send(user, bot)
    }

    // Invalid also
    message { "Also" }.also {
        // No send
    }

    // Deep nesting
    message { "Nested" }.apply {
        run {
            also {
                // No send
            }
        }
    }
}
