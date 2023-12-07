@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnswerCallbackQueryOptions
import eu.vendeli.tgbot.utils.getReturnType

class AnswerCallbackQueryAction(callbackQueryId: String) :
    Action<Boolean>(),
    OptionsFeature<AnswerCallbackQueryAction, AnswerCallbackQueryOptions> {
    override val method = TgMethod("answerCallbackQuery")
    override val returnType = getReturnType()
    override val OptionsFeature<AnswerCallbackQueryAction, AnswerCallbackQueryOptions>.options
        get() = AnswerCallbackQueryOptions()

    init {
        parameters["callback_query_id"] = callbackQueryId
    }
}

fun answerCallbackQuery(callbackQueryId: String) = AnswerCallbackQueryAction(callbackQueryId)
