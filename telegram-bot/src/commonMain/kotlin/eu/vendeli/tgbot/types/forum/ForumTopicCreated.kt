package eu.vendeli.tgbot.types.forum

import eu.vendeli.tgbot.types.common.IsNameImplicitProp
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a new forum topic created in the chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#forumtopiccreated)
 * @property name Name of the topic
 * @property iconColor Color of the topic icon in RGB format
 * @property iconCustomEmojiId Optional. Unique identifier of the custom emoji shown as the topic icon
 */
@Serializable
data class ForumTopicCreated(
    val name: String,
    val iconColor: IconColor,
    val iconCustomEmojiId: String? = null,
    override val isNameImplicit: Boolean? = null,
) : IsNameImplicitProp
