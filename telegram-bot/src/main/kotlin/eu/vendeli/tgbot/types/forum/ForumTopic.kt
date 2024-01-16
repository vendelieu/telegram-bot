package eu.vendeli.tgbot.types.forum

data class ForumTopic(
    val messageThreadId: Int,
    val name: String,
    val iconColor: IconColor,
    val iconCustomEmojiId: String? = null,
)
