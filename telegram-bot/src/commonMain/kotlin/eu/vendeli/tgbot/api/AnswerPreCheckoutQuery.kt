@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class AnswerPreCheckoutQueryAction(
    preCheckoutQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
) : SimpleAction<Boolean>() {
    override val returnType = getReturnType()
    override val method = TgMethod("answerPreCheckoutQuery")

    init {
        parameters["pre_checkout_query_id"] = preCheckoutQueryId.toJsonElement()
        parameters["ok"] = ok.toJsonElement()
        if (errorMessage != null) parameters["error_message"] = errorMessage.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun answerPreCheckoutQuery(preCheckoutQueryId: String, ok: Boolean = true, errorMessage: String? = null) =
    AnswerPreCheckoutQueryAction(preCheckoutQueryId, ok, errorMessage)
