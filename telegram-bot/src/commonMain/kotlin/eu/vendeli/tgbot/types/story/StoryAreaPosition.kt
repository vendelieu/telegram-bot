package eu.vendeli.tgbot.types.story

import kotlinx.serialization.Serializable

/**
 * Describes the position of a clickable area within a story.
 *
 * [Api reference](https://core.telegram.org/bots/api#storyareaposition)
 * @property xPercentage The abscissa of the area's center, as a percentage of the media width
 * @property yPercentage The ordinate of the area's center, as a percentage of the media height
 * @property widthPercentage The width of the area's rectangle, as a percentage of the media width
 * @property heightPercentage The height of the area's rectangle, as a percentage of the media height
 * @property rotationAngle The clockwise rotation angle of the rectangle, in degrees; 0-360
 * @property cornerRadiusPercentage The radius of the rectangle corner rounding, as a percentage of the media width
 */
@Serializable
data class StoryAreaPosition(
    val xPercentage: Double,
    val yPercentage: Double,
    val widthPercentage: Double,
    val heightPercentage: Double,
    val rotationAngle: Double,
    val cornerRadiusPercentage: Double,
)
