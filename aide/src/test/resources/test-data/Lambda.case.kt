// PROCESSOR: ErrorOnlyProcessor
// EXPECTED:
// END

// FILE: InterfaceCases.kt

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message

@CommandHandler(["/start"])
suspend fun interfaceCases(user: User, bot: TelegramBot) {
    val lambda = {
        message { "test" }
    }

    // Valid usage
    lambda().send(user, bot)
}
