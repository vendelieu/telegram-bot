@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.poll.Poll
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class StopPollAction(
    messageId: Long,
) : Action<Poll>(),
    BusinessActionExt<Poll>,
    MarkupFeature<SendPollAction> {
    override val method = TgMethod("stopPoll")
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
@Suppress("NOTHING_TO_INLINE")
inline fun stopPoll(messageId: Long) = StopPollAction(messageId)
