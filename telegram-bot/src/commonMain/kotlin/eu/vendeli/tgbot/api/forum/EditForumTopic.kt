@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditForumTopicAction(
    messageThreadId: Int,
    name: String? = null,
    iconCustomEmojiId: String? = null,
) : Action<Boolean>() {
    override val method = TgMethod("editForumTopic")
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
        if (name != null) parameters["name"] = name.toJsonElement()
        if (iconCustomEmojiId != null) parameters["icon_custom_emoji_id"] = iconCustomEmojiId.toJsonElement()
    }
}

/**
 * Use this method to edit name and icon of a topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 * @param chatId Required 
 * @param messageThreadId Required 
 * @param name New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
 * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all allowed custom emoji identifiers. Pass an empty string to remove the icon. If not specified, the current icon will be kept
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#editforumtopic
*/
@Suppress("NOTHING_TO_INLINE")
inline fun editForumTopic(messageThreadId: Int, name: String? = null, iconCustomEmojiId: String? = null) =
    EditForumTopicAction(messageThreadId, name, iconCustomEmojiId)
