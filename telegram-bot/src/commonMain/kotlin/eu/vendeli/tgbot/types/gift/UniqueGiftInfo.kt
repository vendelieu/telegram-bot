package eu.vendeli.tgbot.types.gift

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Describes a service message about a unique gift that was sent or received.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegiftinfo)
 * @property gift Information about the gift
 * @property origin Origin of the gift. Currently, either "upgrade" for gifts upgraded from regular gifts, "transfer" for gifts transferred from other users or channels, or "resale" for gifts bought from other users
 * @property lastResaleStarCount Optional. For gifts bought from other users, the price paid for the gift
 * @property ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @property transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 * @property nextTransferDate Optional. Point in time (Unix timestamp) when the gift can be transferred. If it is in the past, then the gift can be transferred now
 */
@Serializable
data class UniqueGiftInfo(
    val gift: UniqueGift,
    val origin: GiftOrigin,
    val lastResaleStarCount: Int? = null,
    val ownedGiftId: String? = null,
    val transferStarCount: Int? = null,
    @Serializable(InstantSerializer::class)
    val nextTransferDate: Instant? = null,
)
