package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The state of a payment subscription toward the bot.
 */
@Serializable
enum class SubscriptionState {
    @SerialName("canceled")
    Canceled,

    @SerialName("active")
    Active,

    @SerialName("failed")
    Failed,
}
