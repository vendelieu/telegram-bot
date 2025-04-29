package eu.vendeli.tgbot.types.story

import kotlinx.serialization.Serializable

/**
 * Describes a clickable area on a story media.
 *
 * [Api reference](https://core.telegram.org/bots/api#storyarea)
 * @property position Position of the area
 * @property type Type of the area
 */
@Serializable
data class StoryArea(
    val position: StoryAreaPosition,
    val type: StoryAreaType,
)
