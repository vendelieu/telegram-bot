package eu.vendeli.tgbot.types.story

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.utils.common.cast
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
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

    @TgAPI.Ignore
    internal abstract var file: ImplicitFile

    @Serializable
    @SerialName("photo")
    data class Photo(
        @Serializable(ImplicitFile.Serde::class)
        var photo: ImplicitFile,
    ) : InputStoryContent() {
        init {
            require(photo is ImplicitFile.FileData) {
                "photo must be ImplicitFile.FileData"
            }
        }

        @Transient
        override var file: ImplicitFile = photo.cast()
            set(value) {
                photo = value
                field = value
            }
    }

    @Serializable
    @SerialName("video")
    data class Video(
        @Serializable(ImplicitFile.Serde::class)
        var video: ImplicitFile,
        @Serializable(with = DurationSerializer::class)
        val duration: Duration? = null,
        val coverFrameTimestamp: Double = 0.0,
        val isAnimation: Boolean? = null,
    ) : InputStoryContent() {
        init {
            require(video is ImplicitFile.FileData) {
                "video must be ImplicitFile.FileData"
            }
        }

        @Transient
        override var file: ImplicitFile = video.cast()
            set(value) {
                video = value
                field = value
            }
    }
}
