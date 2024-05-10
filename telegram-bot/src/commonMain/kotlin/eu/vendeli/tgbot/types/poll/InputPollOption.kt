package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.MessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class InputPollOption(
    val text: String,
    val textParseMode: String? = null,
    val textEntities: List<MessageEntity>? = null
)
