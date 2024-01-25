@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.payment.ShippingOption
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class AnswerShippingQueryAction(
    shippingQueryId: String,
    ok: Boolean = true,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("answerShippingQuery")
    override val returnType = getReturnType()

    init {
        parameters["shipping_query_id"] = shippingQueryId.toJsonElement()
        parameters["ok"] = ok.toJsonElement()
        if (shippingOptions != null) parameters["shipping_options"] = shippingOptions.toJsonElement()
        if (errorMessage != null) parameters["error_message"] = errorMessage.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) = AnswerShippingQueryAction(shippingQueryId, ok, shippingOptions, errorMessage)
fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
    shippingOptions: ListingBuilder<ShippingOption>.() -> Unit,
) = answerShippingQuery(shippingQueryId, ok, ListingBuilder.build(shippingOptions), errorMessage)

@Suppress("NOTHING_TO_INLINE")
inline fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
    vararg shippingOption: ShippingOption,
) = answerShippingQuery(shippingQueryId, ok, shippingOption.asList(), errorMessage)
