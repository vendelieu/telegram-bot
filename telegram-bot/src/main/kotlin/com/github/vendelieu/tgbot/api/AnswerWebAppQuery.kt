package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.InlineQueryResult
import com.github.vendelieu.tgbot.types.SentWebAppMessage
import com.github.vendelieu.tgbot.types.internal.TgMethod

class AnswerWebAppQueryAction(webAppQueryId: String, result: InlineQueryResult) : SimpleAction<SentWebAppMessage> {
    override val method: TgMethod = TgMethod("answerWebAppQuery")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["web_app_query_id"] = webAppQueryId
        parameters["result"] = result
    }
}

fun answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult) = AnswerWebAppQueryAction(webAppQueryId, result)
