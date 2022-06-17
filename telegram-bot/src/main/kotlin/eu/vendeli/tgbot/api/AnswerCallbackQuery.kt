package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnswerCallbackQueryOptions

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
