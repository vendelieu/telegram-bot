package eu.vendeli.ktor.starter

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.utils.common.BotConfigurator
import eu.vendeli.tgbot.utils.common.HandlingBehaviourBlock
import kotlin.properties.Delegates

/**
 * Configuration for the bot.
 *
 * @property token Token for the bot.
 * @property pckg Package where commands are searched.
 */
class BotConfiguration {
    internal var configuration: BotConfigurator = {}
    internal var handlingBehaviour: HandlingBehaviourBlock? = null
    internal var onInitHook: suspend (bot: TelegramBot) -> Unit = {}

    var token by Delegates.notNull<String>()

    var pckg: String? = null

    /**
     * Set the bot configuration.
     *
     * @param config Lambda to configure the bot.
     */
    fun configuration(config: BotConfigurator) {
        configuration = config
    }

    /**
     * Set the handling behavior for updates.
     *
     * @param block Lambda to define handling behavior.
     */
    fun handlingBehaviour(block: HandlingBehaviourBlock) {
        handlingBehaviour = block
    }

    /**
     * Set the initialization hook for the bot.
     *
     * @param block Lambda to be executed on bot initialization.
     */
    fun onInit(block: suspend (bot: TelegramBot) -> Unit) {
        onInitHook = block
    }
}
