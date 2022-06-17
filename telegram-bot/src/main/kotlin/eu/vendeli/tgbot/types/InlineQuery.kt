package eu.vendeli.tgbot.types

data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: ChatType? = null,
    val location: LocationContent? = null,
)
