package eu.vendeli.tgbot.types.story

import eu.vendeli.tgbot.types.common.LocationAddress
import eu.vendeli.tgbot.types.common.ReactionType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Describes the type of a clickable area on a story. Currently, it can be one of
 * - StoryAreaTypeLocation
 * - StoryAreaTypeSuggestedReaction
 * - StoryAreaTypeLink
 * - StoryAreaTypeWeather
 * - StoryAreaTypeUniqueGift
 *
 * [Api reference](https://core.telegram.org/bots/api#storyareatype)
 *
 */
@Serializable
sealed class StoryAreaType {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("location")
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val address: LocationAddress? = null,
    ) : StoryAreaType()

    @Serializable
    @SerialName("suggested_reaction")
    data class SuggestedReaction(
        val reactionType: ReactionType,
        val isDark: Boolean? = null,
        val isFlipped: Boolean? = null,
    ) : StoryAreaType()

    @Serializable
    @SerialName("link")
    data class Link(
        val url: String,
    ) : StoryAreaType()

    @Serializable
    @SerialName("weather")
    data class Weather(
        val temperature: Double,
        val emoji: String,
        val backgroundColor: Int,
    ) : StoryAreaType()

    @Serializable
    @SerialName("unique_gift")
    data class UniqueGift(
        val name: String,
    ) : StoryAreaType()
}
