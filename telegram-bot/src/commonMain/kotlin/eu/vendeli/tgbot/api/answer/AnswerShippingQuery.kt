@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.answer

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.payment.ShippingOption
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class AnswerShippingQueryAction(
    shippingQueryId: String,
    ok: Boolean = true,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("answerShippingQuery")
    override val method = "answerShippingQuery"
    override val returnType = getReturnType()

    init {
        parameters["shipping_query_id"] = shippingQueryId.toJsonElement()
        parameters["ok"] = ok.toJsonElement()
        if (shippingOptions != null) parameters["shipping_options"] =
            shippingOptions.encodeWith(ShippingOption.serializer())
        if (errorMessage != null) parameters["error_message"] = errorMessage.toJsonElement()
    }
}

/**
 * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#answershippingquery)
 * @param shippingQueryId Unique identifier for the query to be answered
 * @param ok Pass True if delivery to the specified address is possible and False if there are any problems (for example, if delivery to the specified address is not possible)
 * @param shippingOptions Required if ok is True. A JSON-serialized array of available shipping options.
 * @param errorMessage Required if ok is False. Error message in human readable form that explains why it is impossible to complete the order (e.g. "Sorry, delivery to your desired address is unavailable'). Telegram will display this message to the user.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) = AnswerShippingQueryAction(shippingQueryId, ok, shippingOptions, errorMessage)

@TgAPI
fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
    shippingOptions: ListingBuilder<ShippingOption>.() -> Unit,
) = answerShippingQuery(shippingQueryId, ok, ListingBuilder.build(shippingOptions), errorMessage)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
    vararg shippingOption: ShippingOption,
) = answerShippingQuery(shippingQueryId, ok, shippingOption.asList(), errorMessage)
