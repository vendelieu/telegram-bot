package eu.vendeli.tgbot.types.media

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * This object describes paid media. Currently, it can be one of
 * - PaidMediaPreview
 * - PaidMediaPhoto
 * - PaidMediaVideo
 *
 * [Api reference](https://core.telegram.org/bots/api#paidmedia)
 *
 */
@Serializable
sealed class PaidMedia {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("preview")
    data class Preview(
        val width: Int?,
        val height: Int?,
        val duration: Int?,
    ) : PaidMedia()

    @Serializable
    @SerialName("photo")
    data class Photo(
        val photo: List<PhotoSize>,
    ) : PaidMedia()

    @Serializable
    @SerialName("video")
    data class Video(
        val video: eu.vendeli.tgbot.types.media.Video,
    ) : PaidMedia()
}
