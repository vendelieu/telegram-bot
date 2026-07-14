package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object contains information about changes to a user payment subscription toward the current bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#botsubscriptionupdated)
 * @property user User who subscribed for payments toward the bot
 * @property invoicePayload Bot-specified invoice payload
 * @property state The new state of the subscription. Currently, it can be one of "canceled" if the user canceled the subscription, "active" if the user re-enabled a previously canceled subscription, or "failed" if payment for the subscription failed.
 */
@Serializable
data class BotSubscriptionUpdated(
    val user: User,
    val invoicePayload: String,
    val state: SubscriptionState,
)
