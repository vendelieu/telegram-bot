package eu.vendeli.tgbot.types.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the position on faces where a mask should be placed by default.
 *
 * [Api reference](https://core.telegram.org/bots/api#maskposition)
 * @property point The part of the face relative to which the mask should be placed. One of "forehead", "eyes", "mouth", or "chin".
 * @property xShift Shift by X-axis measured in widths of the mask scaled to the face size, from left to right. For example, choosing -1.0 will place mask just to the left of the default mask position.
 * @property yShift Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom. For example, 1.0 will place the mask just below the default mask position.
 * @property scale Mask scaling coefficient. For example, 2.0 means double size.
 */
@Serializable
data class MaskPosition(
    val point: MaskPoint,
    val xShift: Float,
    val yShift: Float,
    val scale: Float,
)

@Serializable
enum class MaskPoint {
    @SerialName("forehead")
    Forehead,

    @SerialName("eyes")
    Eyes,

    @SerialName("mouth")
    Mouth,

    @SerialName("chin")
    Chin,
}
