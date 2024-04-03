package eu.vendeli.tgbot.types.forum

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about an edited forum topic.
 *
 * Api reference: https://core.telegram.org/bots/api#forumtopicedited
 * @property name Optional. New name of the topic, if it was edited
 * @property iconCustomEmojiId Optional. New identifier of the custom emoji shown as the topic icon, if it was edited; an empty string if the icon was removed
 */
@Serializable
data class ForumTopicEdited(
    val name: String? = null,
    val iconCustomEmojiId: String? = null,
)
