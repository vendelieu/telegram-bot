@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.forum.ForumTopic
import eu.vendeli.tgbot.types.forum.IconColor
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class CreateForumTopicAction(
    name: String,
    iconColor: IconColor? = null,
    iconCustomEmojiId: String? = null,
) : Action<ForumTopic>() {
    override val method = TgMethod("createForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name.toJsonElement()
        if (iconColor != null) parameters["icon_color"] = iconColor.encodeWith(IconColor.serializer())
        if (iconCustomEmojiId != null) parameters["icon_custom_emoji_id"] = iconCustomEmojiId.toJsonElement()
    }
}

/**
 * Use this method to create a topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have the canManageTopics administrator rights.
 * Returns information about the created topic as a [ForumTopic] object.
 *
 */
@Suppress("NOTHING_TO_INLINE")
inline fun createForumTopic(name: String, iconColor: IconColor? = null, iconCustomEmojiId: String? = null) =
    CreateForumTopicAction(name, iconColor, iconCustomEmojiId)
