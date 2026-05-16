@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.answer

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.common.SentGuestMessage
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class AnswerGuestQueryAction(
    guestQueryId: String,
    result: InlineQueryResult,
) : SimpleAction<SentGuestMessage>() {
    @TgAPI.Name("answerGuestQuery")
    override val method = "answerGuestQuery"
    override val returnType = getReturnType()

    init {
        parameters["guest_query_id"] = guestQueryId.toJsonElement()
        parameters["result"] = result.encodeWith(InlineQueryResult.serializer())
    }
}

/**
 * Use this method to send answers to a guest bot query. On success, a SentGuestMessage object is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#answerguestquery)
 * @param guestQueryId Unique identifier for the query to be answered
 * @param result A JSON-serialized object describing the message to be sent
 * @returns [SentGuestMessage]
 */
@TgAPI
inline fun answerGuestQuery(guestQueryId: String, result: InlineQueryResult) =
    AnswerGuestQueryAction(guestQueryId, result)
