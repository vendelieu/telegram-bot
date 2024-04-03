package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.media.Sticker
import kotlinx.serialization.Serializable

@Serializable
data class BusinessIntro(
    val title: String? = null,
    val message: String? = null,
    val sticker: Sticker? = null,
)
