package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * This object describes the colors of the backdrop of a unique gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegiftbackdropcolors)
 * @property centerColor The color in the center of the backdrop in RGB format
 * @property edgeColor The color on the edges of the backdrop in RGB format
 * @property symbolColor The color to be applied to the symbol in RGB format
 * @property textColor The color for the text on the backdrop in RGB format
 */
@Serializable
data class UniqueGiftBackdropColors(
    val centerColor: Int,
    val edgeColor: Int,
    val symbolColor: Int,
    val textColor: Int,
)
