package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import eu.vendeli.tgbot.utils.toImplicitFile
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
@Suppress("OVERRIDE_DEPRECATION")
sealed class InputPaidMedia : ImplicitMediaData {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("photo")
    data class Photo(
        override var media: ImplicitFile,
    ) : InputPaidMedia() {
        constructor(media: String) : this(media.toImplicitFile())
        constructor(media: InputFile) : this(media.toImplicitFile())
    }

    @Serializable
    @SerialName("video")
    data class Video(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val cover: ImplicitFile? = null,
        @Serializable(InstantSerializer::class)
        val startTimestamp: Instant? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
    ) : InputPaidMedia()
}
