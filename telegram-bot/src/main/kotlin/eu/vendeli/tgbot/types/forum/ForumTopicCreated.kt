package eu.vendeli.tgbot.types.forum

/**
 * This object represents a service message about a new forum topic created in the chat.
 *
 * @property name Name of the topic
 * @property iconColor Color of the topic icon in RGB format
 * @property iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon
 */
data class ForumTopicCreated(
    val name: String,
    val iconColor: Int,
    val iconCustomEmojiId: String? = null,
)
