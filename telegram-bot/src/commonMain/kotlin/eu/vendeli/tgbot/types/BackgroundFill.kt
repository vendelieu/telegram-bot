package eu.vendeli.tgbot.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the way a background is filled based on the selected colors. Currently, it can be one of
 * - BackgroundFillSolid
 * - BackgroundFillGradient
 * - BackgroundFillFreeformGradient
 *
 * [Api reference](https://core.telegram.org/bots/api#backgroundfill)
 *
 */
@Serializable
sealed class BackgroundFill {
    @OptIn(ExperimentalSerializationApi::class)
    val type: String by lazy {
        serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("solid")
    data class Solid(
        val color: Int,
    ) : BackgroundFill()

    @Serializable
    @SerialName("gradient")
    data class Gradient(
        val topColor: Int,
        val bottomColor: Int,
        val rotationAngle: Int,
    ) : BackgroundFill()

    @Serializable
    @SerialName("freeform_gradient")
    data class FreeformGradient(
        val colors: List<Int>,
    ) : BackgroundFill()
}
