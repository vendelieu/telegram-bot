package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class MessageAutoDeleteTimerChanged(
    val messageAutoDeleteTime: Int,
)
