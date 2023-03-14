@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.InlineQueryResult
import eu.vendeli.tgbot.types.SentWebAppMessage
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class AnswerWebAppQueryAction(
    webAppQueryId: String,
    result: InlineQueryResult,
) : SimpleAction<SentWebAppMessage>, ActionState() {
    override val method: TgMethod = TgMethod("answerWebAppQuery")
    override val returnType = getReturnType()

    init {
        parameters["web_app_query_id"] = webAppQueryId
        parameters["result"] = result
    }
}

fun answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult) = AnswerWebAppQueryAction(webAppQueryId, result)
