package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.media.Document
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * This object describes the type of a background. Currently, it can be one of
 * - BackgroundTypeFill
 * - BackgroundTypeWallpaper
 * - BackgroundTypePattern
 * - BackgroundTypeChatTheme
 *
 * [Api reference](https://core.telegram.org/bots/api#backgroundtype)
 *
 */
@Serializable
sealed class BackgroundType {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("fill")
    data class Fill(
        val fill: BackgroundFill,
        val darkThemeDimming: Int,
    ) : BackgroundType()

    @Serializable
    @SerialName("wallpaper")
    data class Wallpaper(
        val document: Document,
        val darkThemeDimming: Int,
        val isBlurred: Boolean? = null,
        val isMoving: Boolean? = null,
    ) : BackgroundType()

    @Serializable
    @SerialName("pattern")
    data class Pattern(
        val document: Document,
        val fill: BackgroundFill,
        val intensity: Int,
        val isInverted: Boolean? = null,
        val isMoving: Boolean? = null,
    ) : BackgroundType()

    @Serializable
    @SerialName("chat_theme")
    data class ChatTheme(
        val themeName: String,
    ) : BackgroundType()
}
