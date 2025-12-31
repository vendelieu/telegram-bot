package eu.vendeli.tgbot.types.gift

import eu.vendeli.tgbot.types.common.IsUpgradeSeparateProp
import eu.vendeli.tgbot.types.common.UniqueGiftNumberProp
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.types.user.Gift
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a regular gift that was sent or received.
 *
 * [Api reference](https://core.telegram.org/bots/api#giftinfo)
 * @property gift Information about the gift
 * @property ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @property convertStarCount Optional. Number of Telegram Stars that can be claimed by the receiver by converting the gift; omitted if conversion to Telegram Stars is impossible
 * @property prepaidUpgradeStarCount Optional. Number of Telegram Stars that were prepaid for the ability to upgrade the gift
 * @property isUpgradeSeparate Optional. True, if the gift's upgrade was purchased after the gift was sent
 * @property canBeUpgraded Optional. True, if the gift can be upgraded to a unique gift
 * @property text Optional. Text of the message that was added to the gift
 * @property entities Optional. Special entities that appear in the text
 * @property isPrivate Optional. True, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
 * @property uniqueGiftNumber Optional. Unique number reserved for this gift when upgraded. See the number field in UniqueGift
 */
@Serializable
data class GiftInfo(
    val gift: Gift,
    val ownedGiftId: String? = null,
    val convertStarCount: Int? = null,
    val prepaidUpgradeStarCount: Int? = null,
    val canBeUpgraded: Boolean? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val isPrivate: Boolean? = null,
    override val isUpgradeSeparate: Boolean? = null,
    override val uniqueGiftNumber: Long? = null,
) : IsUpgradeSeparateProp,
    UniqueGiftNumberProp
