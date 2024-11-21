package eu.vendeli.webapps.haptic

import kotlinx.serialization.Serializable

@Serializable
@Suppress("EnumEntryName")
enum class HapticFeedbackStyle {
    light,
    medium,
    heavy,
    rigid,
    soft,
}
