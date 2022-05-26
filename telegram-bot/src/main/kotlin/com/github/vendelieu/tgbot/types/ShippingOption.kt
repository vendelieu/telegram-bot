package com.github.vendelieu.tgbot.types

data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
)
