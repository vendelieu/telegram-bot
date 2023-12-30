package eu.vendeli.tgbot.types

data class TextQuote(
    val text: String,
    val entities: List<MessageEntity>? = null,
    val position: Int,
    val isManual: Boolean? = null
)
