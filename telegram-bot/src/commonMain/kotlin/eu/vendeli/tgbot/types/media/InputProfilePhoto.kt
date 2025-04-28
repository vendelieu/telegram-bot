package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.component.ImplicitFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * This object describes a profile photo to set. Currently, it can be one of
 * - InputProfilePhotoStatic
 * - InputProfilePhotoAnimated
 *
 * [Api reference](https://core.telegram.org/bots/api#inputprofilephoto)
 *
 */
@Serializable
sealed class InputProfilePhoto {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @TgAPI.Ignore
    internal abstract val field: String

    @TgAPI.Ignore
    internal abstract val file: ImplicitFile.InpFile

    @Serializable
    @SerialName("static")
    data class Static(
        val photo: ImplicitFile.InpFile,
    ) : InputProfilePhoto() {
        override val field get() = "photo"
        override val file get() = photo
    }

    @Serializable
    @SerialName("animated")
    data class Animated(
        val animation: ImplicitFile.InpFile,
        val mainFrameTimestamp: Double = 0.0,
    ) : InputProfilePhoto() {
        override val field get() = "animation"
        override val file get() = animation
    }
}
