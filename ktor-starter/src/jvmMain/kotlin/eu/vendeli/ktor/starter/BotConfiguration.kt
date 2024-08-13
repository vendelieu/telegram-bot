package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import kotlin.properties.Delegates

class BotConfiguration {
    internal var configuration: BotConfigurator = {}
    internal var handlingBehaviour: HandlingBehaviourBlock? = null
    internal var onInitHook: suspend (bot: TelegramBot) -> Unit = {}

    var token by Delegates.notNull<String>()
    var pckg: String? = null

    fun configuration(config: BotConfigurator) {
        configuration = config
    }

    fun handlingBehaviour(block: HandlingBehaviourBlock) {
        handlingBehaviour = block
    }

    fun onInit(block: suspend (bot: TelegramBot) -> Unit) {
        onInitHook = block
    }
}
