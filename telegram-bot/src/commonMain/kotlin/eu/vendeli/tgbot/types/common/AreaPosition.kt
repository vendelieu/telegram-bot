package eu.vendeli.tgbot.types.common

import kotlinx.serialization.Serializable

/**
 * Describes the position of a rectangle area on a story media or a TON wallet card.
 *
 * [Api reference](https://core.telegram.org/bots/api#areaposition)
 * @property xPercentage The abscissa of the area's center, as a percentage of the media or the card width
 * @property yPercentage The ordinate of the area's center, as a percentage of the media or the card height
 * @property widthPercentage The width of the area's rectangle, as a percentage of the media or the card width
 * @property heightPercentage The height of the area's rectangle, as a percentage of the media or the card height
 * @property rotationAngle The clockwise rotation angle of the rectangle, in degrees; 0-360
 * @property cornerRadiusPercentage The radius of the rectangle corner rounding, as a percentage of the media or the card width
 */
@Serializable
data class AreaPosition(
    val xPercentage: Double,
    val yPercentage: Double,
    val widthPercentage: Double,
    val heightPercentage: Double,
    val rotationAngle: Double,
    val cornerRadiusPercentage: Double,
)
