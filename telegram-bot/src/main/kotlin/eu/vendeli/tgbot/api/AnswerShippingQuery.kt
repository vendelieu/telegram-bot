package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.ShippingOption
import eu.vendeli.tgbot.types.internal.TgMethod

class AnswerShippingQueryAction(
    shippingQueryId: String,
    ok: Boolean = true,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("answerShippingQuery")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["shipping_query_id"] = shippingQueryId
        parameters["ok"] = ok
        if (shippingOptions != null) parameters["shipping_options"] = shippingOptions
        if (errorMessage != null) parameters["error_message"] = errorMessage
    }
}

fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
    shippingOptions: MutableList<ShippingOption>.() -> Unit,
) = AnswerShippingQueryAction(shippingQueryId, ok, mutableListOf<ShippingOption>().apply(shippingOptions), errorMessage)

fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
    vararg shippingOption: ShippingOption,
) = AnswerShippingQueryAction(shippingQueryId, ok, listOf(*shippingOption), errorMessage)

fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) = AnswerShippingQueryAction(shippingQueryId, ok, shippingOptions, errorMessage)
