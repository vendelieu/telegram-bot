package eu.vendeli.tgbot.types.media

import com.fasterxml.jackson.annotation.JsonProperty

data class MaskPosition(
    val point: MaskPoint,
    @get:JsonProperty("x_shift") val xShift: Float,
    @get:JsonProperty("y_shift") val yShift: Float,
    val scale: Float,
)

enum class MaskPoint(private val literal: String) {
    Forehead("forehead"), Eyes("eyes"), Mouth("mouth"), Chin("chin");

    override fun toString(): String = literal
}
