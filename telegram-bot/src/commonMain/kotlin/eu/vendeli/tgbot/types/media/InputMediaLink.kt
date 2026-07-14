package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Represents an HTTP link to be attached to a poll option.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmedialink)
 * @property url HTTP URL of the link
 */
@Serializable
@SerialName("link")
@TgAPI.Name("InputMediaLink")
data class InputMediaLink(
    val url: String,
) : InputPollOptionMedia {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }
}
