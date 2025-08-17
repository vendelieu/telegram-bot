@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class DeclineSuggestedPostAction(
    messageId: Long,
    comment: String? = null,
) : Action<Boolean>() {
    @TgAPI.Name("declineSuggestedPost")
    override val method = "declineSuggestedPost"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
        if (comment != null) parameters["comment"] = comment.toJsonElement()
    }
}

/**
 * Use this method to decline a suggested post in a direct messages chat. The bot must have the 'can_manage_direct_messages' administrator right in the corresponding channel chat. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#declinesuggestedpost)
 * @param chatId Unique identifier for the target direct messages chat
 * @param messageId Identifier of a suggested post message to decline
 * @param comment Comment for the creator of the suggested post; 0-128 characters
 * @returns [Boolean]
 */
@TgAPI
inline fun declineSuggestedPost(messageId: Long, comment: String? = null) =
    DeclineSuggestedPostAction(messageId, comment)
