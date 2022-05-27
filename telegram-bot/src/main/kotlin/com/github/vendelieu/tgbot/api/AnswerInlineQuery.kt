package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.InlineQueryResult
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.AnswerInlineQueryOptions

class AnswerInlineQueryAction(inlineQueryId: String, results: List<InlineQueryResult>) :
    SimpleAction<Boolean>, OptionAble, OptionsFeature<AnswerInlineQueryAction, AnswerInlineQueryOptions> {
    override val method: TgMethod = TgMethod("answerInlineQuery")
    override var options = AnswerInlineQueryOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["inline_query_id"] = inlineQueryId
        parameters["results"] = results
    }
}

fun answerInlineQuery(inlineQueryId: String, results: MutableList<InlineQueryResult>.() -> Unit) =
    AnswerInlineQueryAction(inlineQueryId, mutableListOf<InlineQueryResult>().apply(results))

fun answerInlineQuery(inlineQueryId: String, vararg result: InlineQueryResult) =
    AnswerInlineQueryAction(inlineQueryId, listOf(*result))

fun answerInlineQuery(inlineQueryId: String, results: List<InlineQueryResult>) =
    AnswerInlineQueryAction(inlineQueryId, results)
