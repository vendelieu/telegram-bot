package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.media.Document
import kotlinx.serialization.Serializable

@Serializable
sealed class BackgroundType(val type: String) {
    @Serializable
    data class Fill(val fill: BackgroundFill, val darkThemeDimming: Int? = null) : BackgroundType("fill")

    @Serializable
    data class Wallpaper(
        val document: Document,
        val darkThemeDimming: Int? = null,
        val isBlurred: Boolean? = null,
        val isMoving: Boolean? = null,
    ) : BackgroundType("wallpaper")

    @Serializable
    data class Pattern(
        val document: Document,
        val fill: BackgroundFill,
        val intensity: Int? = null,
        val isInverted: Boolean? = null,
        val isMoving: Boolean? = null,
    ) : BackgroundType("pattern")

    @Serializable
    data class ChatTheme(val themeName: String) : BackgroundType("chat_theme")
}
