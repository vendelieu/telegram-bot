package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class RefundStarPaymentAction(
    telegramPaymentChargeId: String,
    userId: Long,
) : SimpleAction<Boolean>() {
    override val method = TgMethod("refundStarPayment")
    override val returnType = getReturnType()

    init {
        parameters["telegram_payment_charge_id"] = telegramPaymentChargeId.toJsonElement()
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Refunds a successful payment in Telegram Stars. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#refundstarpayment)
 * @param userId Identifier of the user whose payment will be refunded
 * @param telegramPaymentChargeId Telegram payment identifier
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun refundStarPayment(telegramPaymentChargeId: String, userId: Long) =
    RefundStarPaymentAction(telegramPaymentChargeId, userId)
