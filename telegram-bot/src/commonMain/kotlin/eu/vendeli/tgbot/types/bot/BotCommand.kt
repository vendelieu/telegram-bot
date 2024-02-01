package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.interfaces.MultipleResponse
import kotlinx.serialization.Serializable

@Serializable
data class BotCommand(
    val command: String,
    val description: String,
) : MultipleResponse
