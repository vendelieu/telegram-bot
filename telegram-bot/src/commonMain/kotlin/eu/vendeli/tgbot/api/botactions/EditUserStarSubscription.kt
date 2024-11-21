@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class EditUserStarSubscriptionAction(
    userId: Long,
    telegramPaymentChargeId: String,
    isCanceled: Boolean,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("editUserStarSubscription")
    override val method = "editUserStarSubscription"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["telegram_payment_charge_id"] = telegramPaymentChargeId.toJsonElement()
        parameters["is_canceled"] = isCanceled.toJsonElement()
    }
}

/**
 * Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#edituserstarsubscription)
 * @param userId Identifier of the user whose subscription will be edited
 * @param telegramPaymentChargeId Telegram payment identifier for the subscription
 * @param isCanceled Pass True to cancel extension of the user subscription; the subscription must be active up to the end of the current subscription period. Pass False to allow the user to re-enable a subscription that was previously canceled by the bot.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editUserStarSubscription(
    userId: Long,
    telegramPaymentChargeId: String,
    isCanceled: Boolean,
) = EditUserStarSubscriptionAction(userId, telegramPaymentChargeId, isCanceled)
