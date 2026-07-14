@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.chat.JoinRequestQueryResult
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class AnswerChatJoinRequestQueryAction(
    chatJoinRequestQueryId: String,
    result: JoinRequestQueryResult,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("answerChatJoinRequestQuery")
    override val method = "answerChatJoinRequestQuery"
    override val returnType = getReturnType()

    init {
        parameters["chat_join_request_query_id"] = chatJoinRequestQueryId.toJsonElement()
        parameters["result"] = result.encodeWith(JoinRequestQueryResult.serializer())
    }
}

/**
 * Use this method to process a received chat join request query. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#answerchatjoinrequestquery)
 * @param chatJoinRequestQueryId Unique identifier of the join request query
 * @param result Result of the query. Must be either "approve" to allow the user to join the chat, "decline" to disallow the user to join the chat, or "queue" to leave the decision to other administrators.
 * @returns [Boolean]
 */
@TgAPI
inline fun answerChatJoinRequestQuery(chatJoinRequestQueryId: String, result: JoinRequestQueryResult) =
    AnswerChatJoinRequestQueryAction(chatJoinRequestQueryId, result)
