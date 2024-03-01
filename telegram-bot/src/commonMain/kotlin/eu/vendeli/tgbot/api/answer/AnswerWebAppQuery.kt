@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.answer

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.SentWebAppMessage
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement

class AnswerWebAppQueryAction(
    webAppQueryId: String,
    result: InlineQueryResult,
) : SimpleAction<SentWebAppMessage>() {
    override val method = TgMethod("answerWebAppQuery")
    override val returnType = getReturnType()

    init {
        parameters["web_app_query_id"] = webAppQueryId.toJsonElement()
        parameters["result"] = result.encodeWith(DynamicLookupSerializer)
    }
}

/**
 * Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of the user to the chat from which the query originated. On success, a SentWebAppMessage object is returned.
 * @param webAppQueryId Required 
 * @param result Required 
 * @returns [SentWebAppMessage]
 * Api reference: https://core.telegram.org/bots/api#answerwebappquery
*/
@Suppress("NOTHING_TO_INLINE")
inline fun answerWebAppQuery(
    webAppQueryId: String,
    result: InlineQueryResult,
) = AnswerWebAppQueryAction(webAppQueryId, result)
