package eu.vendeli.tgbot.types.giveaway

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the creation of a scheduled giveaway.
 *
 * [Api reference](https://core.telegram.org/bots/api#giveawaycreated)
 * @property prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 */
@Serializable
data class GiveawayCreated(
    val prizeStarCount: Int? = null,
)
