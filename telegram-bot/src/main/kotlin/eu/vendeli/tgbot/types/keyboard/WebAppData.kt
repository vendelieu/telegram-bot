package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

@Serializable
data class WebAppData(
    val data: String? = null,
    val buttonText: String,
)
