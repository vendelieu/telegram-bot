package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

@Serializable
sealed class PaidMedia(val type: String) {
    @Serializable
    data class Preview(val width: Int?, val height: Int?, val duration: Int?) : PaidMedia("preview")

    @Serializable
    data class Photo(val photo: List<PhotoSize>) : PaidMedia("photo")

    @Serializable
    data class Video(val video: eu.vendeli.tgbot.types.media.Video) : PaidMedia("video")
}
