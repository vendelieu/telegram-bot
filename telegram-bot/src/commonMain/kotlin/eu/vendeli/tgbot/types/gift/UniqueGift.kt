package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * This object describes a unique gift that was upgraded from a regular gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegift)
 * @property baseName Human-readable name of the regular gift from which this unique gift was upgraded
 * @property name Unique name of the gift. This name can be used in https://t.me/nft/... links and story areas
 * @property number Unique number of the upgraded gift among gifts upgraded from the same regular gift
 * @property model Model of the gift
 * @property symbol Symbol of the gift
 * @property backdrop Backdrop of the gift
 */
@Serializable
data class UniqueGift(
    val baseName: String,
    val name: String,
    val number: Int,
    val model: UniqueGiftModel,
    val symbol: UniqueGiftSymbol,
    val backdrop: UniqueGiftBackdrop,
)
