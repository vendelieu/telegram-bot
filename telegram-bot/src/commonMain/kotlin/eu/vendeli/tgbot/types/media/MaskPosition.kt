package eu.vendeli.tgbot.types.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
