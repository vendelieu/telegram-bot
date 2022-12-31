@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.InlineQueryResult
import eu.vendeli.tgbot.types.SentWebAppMessage
import eu.vendeli.tgbot.types.internal.TgMethod

class AnswerWebAppQueryAction(webAppQueryId: String, result: InlineQueryResult) : SimpleAction<SentWebAppMessage> {
    override val method: TgMethod = TgMethod("answerWebAppQuery")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["web_app_query_id"] = webAppQueryId
        parameters["result"] = result
    }
}

fun answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult) = AnswerWebAppQueryAction(webAppQueryId, result)
