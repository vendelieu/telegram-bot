package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

@Serializable
data class CopyTextButton(
    val text: String
)
