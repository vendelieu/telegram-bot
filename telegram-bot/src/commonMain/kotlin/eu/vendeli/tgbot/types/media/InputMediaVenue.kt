package eu.vendeli.tgbot.types.media

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Represents a venue to be sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmediavenue)
 */
@Serializable
@SerialName("venue")
data class InputMediaVenue(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
) : InputPollMedia,
    InputPollOptionMedia {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }
}
