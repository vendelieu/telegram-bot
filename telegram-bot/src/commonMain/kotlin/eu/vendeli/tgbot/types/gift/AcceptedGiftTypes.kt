package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * This object describes the types of gifts that can be gifted to a user or a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#acceptedgifttypes)
 * @property unlimitedGifts True, if unlimited regular gifts are accepted
 * @property limitedGifts True, if limited regular gifts are accepted
 * @property uniqueGifts True, if unique gifts or gifts that can be upgraded to unique for free are accepted
 * @property premiumSubscription True, if a Telegram Premium subscription is accepted
 * @property giftsFromChannels True, if gifts from channels are accepted
 */
@Serializable
data class AcceptedGiftTypes(
    val unlimitedGifts: Boolean,
    val limitedGifts: Boolean,
    val uniqueGifts: Boolean,
    val premiumSubscription: Boolean,
    val giftsFromChannels: Boolean,
)
