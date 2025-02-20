package eu.vendeli.tgbot.types.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DiceEmoji(
    val emoji: String,
) {
    @SerialName("🎲")
    DICE("🎲"),

    @SerialName("🎯")
    BULLSEYE("🎯"),

    @SerialName("🏀")
    BASKETBALL("🏀"),

    @SerialName("⚽")
    SOCCER("⚽"),

    @SerialName("🎳")
    BOWLING("🎳"),

    @SerialName("🎰")
    SLOTS("🎰"),
}
