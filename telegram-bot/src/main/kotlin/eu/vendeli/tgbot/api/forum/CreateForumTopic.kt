@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.forum.ForumTopic
import eu.vendeli.tgbot.types.forum.IconColor
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Use this method to create a topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have the canManageTopics administrator rights.
 * Returns information about the created topic as a [ForumTopic] object.
 *
 */
class CreateForumTopicAction(
    name: String,
    iconColor: IconColor? = null,
    iconCustomEmojiId: String? = null,
) : Action<ForumTopic>() {
    override val method = TgMethod("createForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["name"] = name
        if (iconColor != null) parameters["icon_color"] = iconColor
        if (iconCustomEmojiId != null) parameters["icon_custom_emoji_id"] = iconCustomEmojiId
    }
}

/**
 * Use this method to create a topic in a forum supergroup chat.
 * The bot must be an administrator in the chat for this to work and must have the canManageTopics administrator rights.
 * Returns information about the created topic as a [ForumTopic] object.
 *
 */
fun createForumTopic(name: String, iconColor: IconColor? = null, iconCustomEmojiId: String? = null) =
    CreateForumTopicAction(name, iconColor, iconCustomEmojiId)
