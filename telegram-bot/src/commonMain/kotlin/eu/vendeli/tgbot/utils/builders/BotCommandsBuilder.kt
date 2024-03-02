package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.bot.BotCommand

/**
 * Builder used to add commands to setMyCommands()
 */
class BotCommandsBuilder {
    private val commands: MutableList<BotCommand> = arrayListOf()

    /**
     * Add new bot command
     *
     * @param command
     * @param description
     */
    fun botCommand(command: String, description: String) {
        commands += BotCommand(command, description)
    }

    /**
     * Infix function to add new command
     *
     * @param description
     */
    infix fun String.description(description: String) {
        commands += BotCommand(this, description)
    }

    internal companion object {
        fun build(block: BotCommandsBuilder.() -> Unit): List<BotCommand> = BotCommandsBuilder().apply(block).commands
    }
}
