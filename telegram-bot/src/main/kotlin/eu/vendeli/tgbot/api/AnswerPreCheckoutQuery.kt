@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class AnswerPreCheckoutQueryAction(
    preCheckoutQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
) : SimpleAction<Boolean>() {
    override val returnType = getReturnType()
    override val method = TgMethod("answerPreCheckoutQuery")

    init {
        parameters["pre_checkout_query_id"] = preCheckoutQueryId
        parameters["ok"] = ok
        if (errorMessage != null) parameters["error_message"] = errorMessage
    }
}

fun answerPreCheckoutQuery(preCheckoutQueryId: String, ok: Boolean = true, errorMessage: String? = null) =
    AnswerPreCheckoutQueryAction(preCheckoutQueryId, ok, errorMessage)
