package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.DEFAULT_HANDLING_BEHAVIOUR
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import kotlin.properties.Delegates

class BotConfiguration {
    internal var configuration: BotConfigurator = {}
    internal var handlingBehaviour: HandlingBehaviourBlock = DEFAULT_HANDLING_BEHAVIOUR
    internal var onInitHook: suspend (bot: TelegramBot) -> Unit = {}

    var token by Delegates.notNull<String>()
    var pckg: String? = null
    var identifier by Delegates.notNull<String>()

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
