package eu.vendeli.tgbot.types.media

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Represents a location to be sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmedialocation)
 * @property latitude Latitude of the location
 * @property longitude Longitude of the location
 * @property horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 */
@Serializable
@SerialName("location")
data class InputMediaLocation(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
) : InputPollMedia,
    InputPollOptionMedia {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }
}
