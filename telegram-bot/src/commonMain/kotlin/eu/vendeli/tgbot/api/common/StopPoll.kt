@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.common

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.poll.Poll
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class StopPollAction(
    messageId: Long,
) : Action<Poll>(),
    BusinessActionExt<Poll>,
    MarkupFeature<SendPollAction> {
    @TgAPI.Name("stopPoll")
    override val method = "stopPoll"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to stop a poll which was sent by the bot. On success, the stopped Poll is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#stoppoll)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of the original message with the poll
 * @param replyMarkup A JSON-serialized object for a new message inline keyboard.
 * @returns [Poll]
 */
@TgAPI
inline fun stopPoll(messageId: Long) = StopPollAction(messageId)
