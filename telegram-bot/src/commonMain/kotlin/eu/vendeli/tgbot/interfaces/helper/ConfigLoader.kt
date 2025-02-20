package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.types.configuration.BotConfiguration

/**
 * An interface for configuring the bot.
 *
 * @property token Token of your bot
 * @property commandsPackage The place where the search for commands and inputs will be done.
 */
interface ConfigLoader {
    val token: String
    val commandsPackage: String? get() = null

    /**
     * Function to load bot configuration class.
     */
    fun load(): BotConfiguration
}
