@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class ApproveSuggestedPostAction(
    messageId: Long,
    sendDate: Int? = null,
) : Action<Boolean>() {
    @TgAPI.Name("approveSuggestedPost")
    override val method = "approveSuggestedPost"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
        if (sendDate != null) parameters["send_date"] = sendDate.toJsonElement()
    }
}

/**
 * Use this method to approve a suggested post in a direct messages chat. The bot must have the 'can_post_messages' administrator right in the corresponding channel chat. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#approvesuggestedpost)
 * @param chatId Unique identifier for the target direct messages chat
 * @param messageId Identifier of a suggested post message to approve
 * @param sendDate Point in time (Unix timestamp) when the post is expected to be published; omit if the date has already been specified when the suggested post was created. If specified, then the date must be not more than 2678400 seconds (30 days) in the future
 * @returns [Boolean]
 */
@TgAPI
inline fun approveSuggestedPost(messageId: Long, sendDate: Int? = null) =
    ApproveSuggestedPostAction(messageId, sendDate)
