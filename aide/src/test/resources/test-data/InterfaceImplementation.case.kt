// PROCESSOR: ErrorOnlyProcessor
// EXPECTED:
// END

// FILE: InterfaceCases.kt

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.interfaces.action.Action

interface ActionProvider {
    fun createAction(): Action<*>
}

@CommandHandler(["/start"])
suspend fun interfaceCases(user: User, bot: TelegramBot) {
    val provider = object : ActionProvider {
        override fun createAction() = message { "Interface" }
    }

    // Should warn about missing send
    provider.createAction()

    // Valid usage
    provider.createAction().send(user, bot)
}
