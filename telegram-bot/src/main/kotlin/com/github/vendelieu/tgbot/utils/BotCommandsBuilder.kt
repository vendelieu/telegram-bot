package com.github.vendelieu.tgbot.utils

import com.github.vendelieu.tgbot.types.BotCommand

class BotCommandsBuilder {
    internal val commandsList = mutableListOf<BotCommand>()
    fun botCommand(command: String, description: String) {
        commandsList.add(BotCommand(command, description))
    }
}
