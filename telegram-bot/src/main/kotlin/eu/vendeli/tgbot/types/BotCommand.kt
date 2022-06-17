package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.MultipleResponse

data class BotCommand(
    val command: String,
    val description: String,
) : MultipleResponse
