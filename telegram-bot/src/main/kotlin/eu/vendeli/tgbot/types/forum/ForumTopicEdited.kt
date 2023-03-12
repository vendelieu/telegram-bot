package eu.vendeli.tgbot.types.forum

/**
 * This object represents a service message about an edited forum topic.
 *
 * @param name New name of the topic, if it was edited
 * @param iconCustomEmojiId Optional. New identifier of the custom emoji shown as the topic icon, if it was edited;
 * an empty string if the icon was removed
 */
data class ForumTopicEdited(
    val name: String,
    val iconCustomEmojiId: String,
)
