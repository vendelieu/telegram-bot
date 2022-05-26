package com.github.vendelieu.tgbot.types

data class PreCheckoutQuery(
    val id: String,
    val from: User,
    val currency: Currency,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo?,
)
