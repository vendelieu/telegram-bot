package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.AnswerCallbackQueryOptions

class AnswerCallbackQueryAction(callbackQueryId: String) :
    Action<Boolean>,
    OptionAble,
    OptionsFeature<AnswerCallbackQueryAction, AnswerCallbackQueryOptions> {
    override val method: TgMethod = TgMethod("answerCallbackQuery")
    override var options = AnswerCallbackQueryOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["callback_query_id"] = callbackQueryId
    }
}

fun answerCallbackQuery(callbackQueryId: String) = AnswerCallbackQueryAction(callbackQueryId)
