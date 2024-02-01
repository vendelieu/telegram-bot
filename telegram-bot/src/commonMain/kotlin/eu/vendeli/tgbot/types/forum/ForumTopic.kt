package eu.vendeli.tgbot.types.forum

import kotlinx.serialization.Serializable

@Serializable
data class ForumTopic(
    val messageThreadId: Int,
    val name: String,
    val iconColor: IconColor,
    val iconCustomEmojiId: String? = null,
)
