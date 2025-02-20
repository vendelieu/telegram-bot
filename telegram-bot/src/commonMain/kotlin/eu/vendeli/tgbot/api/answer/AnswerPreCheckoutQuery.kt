@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.answer

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class AnswerPreCheckoutQueryAction(
    preCheckoutQueryId: String,
    ok: Boolean = true,
    errorMessage: String? = null,
) : SimpleAction<Boolean>() {
    override val returnType = getReturnType()

    @TgAPI.Name("answerPreCheckoutQuery")
    override val method = "answerPreCheckoutQuery"

    init {
        parameters["pre_checkout_query_id"] = preCheckoutQueryId.toJsonElement()
        parameters["ok"] = ok.toJsonElement()
        if (errorMessage != null) parameters["error_message"] = errorMessage.toJsonElement()
    }
}

/**
 * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries. On success, True is returned. Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#answerprecheckoutquery)
 * @param preCheckoutQueryId Unique identifier for the query to be answered
 * @param ok Specify True if everything is alright (goods are available, etc.) and the bot is ready to proceed with the order. Use False if there are any problems.
 * @param errorMessage Required if ok is False. Error message in human readable form that explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details. Please choose a different color or garment!"). Telegram will display this message to the user.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun answerPreCheckoutQuery(preCheckoutQueryId: String, ok: Boolean = true, errorMessage: String? = null) =
    AnswerPreCheckoutQueryAction(preCheckoutQueryId, ok, errorMessage)
