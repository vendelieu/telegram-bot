package eu.vendeli.tgbot.types

data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val location: LocationContent? = null,
    val inlineMessageId: String? = null,
    val query: String,
)
