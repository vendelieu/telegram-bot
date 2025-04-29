package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.utils.common.cast
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
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
    internal abstract var file: ImplicitFile

    @Serializable
    @SerialName("static")
    data class Static(
        var photo: ImplicitFile,
    ) : InputProfilePhoto() {
        init {
            require(photo is ImplicitFile.InpFile) {
                "photo must be ImplicitFile.InpFile"
            }
        }

        @Transient
        override var file: ImplicitFile = photo.cast()
            set(value) {
                photo = value
            }
    }

    @Serializable
    @SerialName("animated")
    data class Animated(
        var animation: ImplicitFile,
        val mainFrameTimestamp: Double = 0.0,
    ) : InputProfilePhoto() {
        init {
            require(animation is ImplicitFile.InpFile) {
                "animation must be ImplicitFile.InpFile"
            }
        }

        @Transient
        override var file: ImplicitFile = animation.cast()
            set(value) {
                animation = value
            }
    }
}
