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
 * Use this method to create a topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns information about the created topic as a ForumTopic object.
 * Api reference: https://core.telegram.org/bots/api#createforumtopic
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param name Topic name, 1-128 characters
 * @param iconColor Color of the topic icon in RGB format. Currently, must be one of 7322096 (0x6FB9F0), 16766590 (0xFFD67E), 13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2), or 16478047 (0xFB6F5F)
 * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all allowed custom emoji identifiers.
 * @returns [ForumTopic]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun createForumTopic(name: String, iconColor: IconColor? = null, iconCustomEmojiId: String? = null) =
    CreateForumTopicAction(name, iconColor, iconCustomEmojiId)
