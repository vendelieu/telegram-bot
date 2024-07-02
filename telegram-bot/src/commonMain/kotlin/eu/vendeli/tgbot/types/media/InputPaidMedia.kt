package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.ImplicitMediaData
import eu.vendeli.tgbot.types.internal.ImplicitFile
import kotlinx.serialization.Serializable

@Serializable
@Suppress("OVERRIDE_DEPRECATION")
sealed class InputPaidMedia(
    val type: String,
) : ImplicitMediaData {
    @Serializable
    data class Photo(
        override var media: ImplicitFile,
    ) : InputPaidMedia("photo")

    @Serializable
    data class Video(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
    ) : InputPaidMedia("video")
}
