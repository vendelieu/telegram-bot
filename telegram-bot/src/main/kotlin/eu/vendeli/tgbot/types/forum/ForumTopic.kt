package eu.vendeli.tgbot.types.forum

data class ForumTopic(
    val messageThreadId: Int,
    val name: String,
    val iconColor: Int,
    val iconCustomEmojiId: String? = null,
)
