package eu.vendeli.tgbot.types.story

import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.time.Duration

/**
 * This object describes the content of a story to post. Currently, it can be one of
 * - InputStoryContentPhoto
 * - InputStoryContentVideo
 *
 * [Api reference](https://core.telegram.org/bots/api#inputstorycontent)
 *
 */
@Serializable
sealed class InputStoryContent {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("photo")
    data class Photo(
        val photo: String,
    ) : InputStoryContent()

    @Serializable
    @SerialName("video")
    data class Video(
        val video: String,
        @Serializable(with = DurationSerializer::class)
        val duration: Duration? = null,
        val coverFrameTimestamp: Double = 0.0,
        val isAnimation: Boolean? = null,
    ) : InputStoryContent()
}
