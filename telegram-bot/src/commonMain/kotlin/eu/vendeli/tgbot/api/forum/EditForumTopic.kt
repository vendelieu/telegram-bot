@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.forum

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class EditForumTopicAction(
    messageThreadId: Int,
    name: String? = null,
    iconCustomEmojiId: String? = null,
) : Action<Boolean>() {
    override val method = "editForumTopic"
    override val returnType = getReturnType()

    init {
        parameters["message_thread_id"] = messageThreadId.toJsonElement()
        if (name != null) parameters["name"] = name.toJsonElement()
        if (iconCustomEmojiId != null) parameters["icon_custom_emoji_id"] = iconCustomEmojiId.toJsonElement()
    }
}

/**
 * Use this method to edit name and icon of a topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#editforumtopic)
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 * @param name New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
 * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all allowed custom emoji identifiers. Pass an empty string to remove the icon. If not specified, the current icon will be kept
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editForumTopic(messageThreadId: Int, name: String? = null, iconCustomEmojiId: String? = null) =
    EditForumTopicAction(messageThreadId, name, iconCustomEmojiId)
