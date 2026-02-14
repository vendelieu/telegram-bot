package eu.vendeli.tgbot.types.gift

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.common.IsPremiumProp
import kotlinx.serialization.Serializable

/**
 * This object describes a unique gift that was upgraded from a regular gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegift)
 * @property giftId Identifier of the regular gift from which the gift was upgraded
 * @property baseName Human-readable name of the regular gift from which this unique gift was upgraded
 * @property name Unique name of the gift. This name can be used in https://t.me/nft/... links and story areas
 * @property number Unique number of the upgraded gift among gifts upgraded from the same regular gift
 * @property model Model of the gift
 * @property symbol Symbol of the gift
 * @property backdrop Backdrop of the gift
 * @property isPremium Optional. True, if the original regular gift was exclusively purchaseable by Telegram Premium subscribers
 * @property isBurned Optional. True, if the gift was used to craft another gift and isn't available anymore
 * @property isFromBlockchain Optional. True, if the gift is assigned from the TON blockchain and can't be resold or transferred in Telegram
 * @property colors Optional. The color scheme that can be used by the gift's owner for the chat's name, replies to messages and link previews; for business account gifts and gifts that are currently on sale only
 * @property publisherChat Optional. Information about the chat that published the gift
 */
@Serializable
data class UniqueGift(
    val giftId: String,
    val baseName: String,
    val name: String,
    val number: Int,
    val model: UniqueGiftModel,
    val symbol: UniqueGiftSymbol,
    val backdrop: UniqueGiftBackdrop,
    val isFromBlockchain: Boolean? = null,
    val isBurned: Boolean? = null,
    val colors: UniqueGiftColors? = null,
    val publisherChat: Chat? = null,
    override val isPremium: Boolean? = null,
) : IsPremiumProp
