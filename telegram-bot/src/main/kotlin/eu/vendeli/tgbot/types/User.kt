package eu.vendeli.tgbot.types

data class User(
    val id: Long,
    val isBot: Boolean,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
    val canJoinGroups: Boolean? = null,
    val canReadAllGroupMessages: Boolean? = null,
    val supportsInlineQueries: Boolean? = null,
) {
    fun isPresent() = id > 0
}
