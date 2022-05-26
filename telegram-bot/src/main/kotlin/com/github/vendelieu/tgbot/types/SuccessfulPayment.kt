package com.github.vendelieu.tgbot.types

data class SuccessfulPayment(
    val currency: Currency,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String,
)
