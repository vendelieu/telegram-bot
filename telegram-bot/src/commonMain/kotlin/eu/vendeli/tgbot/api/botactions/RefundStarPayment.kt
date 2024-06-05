package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class RefundStarPaymentAction(
    telegramPaymentChargeId: String,
) : Action<Boolean>() {
    override val method = TgMethod("refundStarPayment")
    override val returnType = getReturnType()

    init {
        parameters["telegram_payment_charge_id"] = telegramPaymentChargeId.toJsonElement()
    }
}

inline fun refundStarPayment(telegramPaymentChargeId: String) = RefundStarPaymentAction(telegramPaymentChargeId)
