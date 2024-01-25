@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.SentWebAppMessage
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class AnswerWebAppQueryAction(
    webAppQueryId: String,
    result: InlineQueryResult,
) : SimpleAction<SentWebAppMessage>() {
    override val method = TgMethod("answerWebAppQuery")
    override val returnType = getReturnType()

    init {
        parameters["web_app_query_id"] = webAppQueryId.toJsonElement()
        parameters["result"] = result.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun answerWebAppQuery(
    webAppQueryId: String,
    result: InlineQueryResult,
) = AnswerWebAppQueryAction(webAppQueryId, result)
