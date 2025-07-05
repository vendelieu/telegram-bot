@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.answer

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.webapp.SentWebAppMessage
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class AnswerWebAppQueryAction(
    webAppQueryId: String,
    result: InlineQueryResult,
) : SimpleAction<SentWebAppMessage>() {
    @TgAPI.Name("answerWebAppQuery")
    override val method = "answerWebAppQuery"
    override val returnType = getReturnType()

    init {
        parameters["web_app_query_id"] = webAppQueryId.toJsonElement()
        parameters["result"] = result.encodeWith(InlineQueryResult.serializer())
    }
}

/**
 * Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of the user to the chat from which the query originated. On success, a SentWebAppMessage object is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#answerwebappquery)
 * @param webAppQueryId Unique identifier for the query to be answered
 * @param result A JSON-serialized object describing the message to be sent
 * @returns [SentWebAppMessage]
 */
@TgAPI
inline fun answerWebAppQuery(
    webAppQueryId: String,
    result: InlineQueryResult,
) = AnswerWebAppQueryAction(webAppQueryId, result)
