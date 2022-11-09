package eu.vendeli.tgbot.types

data class ForumTopicCreated(
    val name: String,
    val iconColor: Int,
    val iconCustomEmojiId: String? = null
)