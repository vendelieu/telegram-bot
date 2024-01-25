package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class TextQuote(
    val text: String,
    val entities: List<MessageEntity>? = null,
    val position: Int,
    val isManual: Boolean? = null,
)
