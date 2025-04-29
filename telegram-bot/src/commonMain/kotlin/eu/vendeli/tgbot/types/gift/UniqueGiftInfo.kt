package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a unique gift that was sent or received.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegiftinfo)
 * @property gift Information about the gift
 * @property origin Origin of the gift. Currently, either "upgrade" or "transfer"
 * @property ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @property transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 */
@Serializable
data class UniqueGiftInfo(
    val gift: UniqueGift,
    val origin: String,
    val ownedGiftId: String? = null,
    val transferStarCount: Int? = null,
)
