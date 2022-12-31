package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.BotCommand

/**
 * Builder used to add commands to setMyCommands()
 */
class BotCommandsBuilder {
    internal val commandsList = mutableListOf<BotCommand>()

    /**
     * Add new bot command
     *
     * @param command
     * @param description
     */
    fun botCommand(command: String, description: String) {
        commandsList.add(BotCommand(command, description))
    }
}
