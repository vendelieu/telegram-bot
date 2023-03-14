@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnswerCallbackQueryOptions
import eu.vendeli.tgbot.utils.getReturnType

class AnswerCallbackQueryAction(callbackQueryId: String) :
    Action<Boolean>,
    ActionState(),
    OptionsFeature<AnswerCallbackQueryAction, AnswerCallbackQueryOptions> {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("answerCallbackQuery")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()
    override val OptionsFeature<AnswerCallbackQueryAction, AnswerCallbackQueryOptions>.options
        get() = AnswerCallbackQueryOptions()

    init {
        parameters["callback_query_id"] = callbackQueryId
    }
}

fun answerCallbackQuery(callbackQueryId: String) = AnswerCallbackQueryAction(callbackQueryId)
