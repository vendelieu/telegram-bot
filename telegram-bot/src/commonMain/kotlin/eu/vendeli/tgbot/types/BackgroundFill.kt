package eu.vendeli.tgbot.types

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
sealed class BackgroundFill(val type: String) {
    @Serializable
    data class Solid(val color: Int) : BackgroundFill("solid")

    @Serializable
    data class Gradient(
        val topColor: Int,
        val bottomColor: Int,
        val rotationAngle: Int? = null,
    ) : BackgroundFill("gradient")

    @Serializable
    data class FreeformGradient(val colors: List<Int>) : BackgroundFill("freeform_gradient")
}
