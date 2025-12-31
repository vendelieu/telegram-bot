package eu.vendeli.tgbot.types.user

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.common.IsPremiumProp
import eu.vendeli.tgbot.types.gift.GiftBackground
import eu.vendeli.tgbot.types.media.Sticker
import kotlinx.serialization.Serializable

/**
 * This object represents a gift that can be sent by the bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#gift)
 * @property id Unique identifier of the gift
 * @property sticker The sticker that represents the gift
 * @property starCount The number of Telegram Stars that must be paid to send the sticker
 * @property upgradeStarCount Optional. The number of Telegram Stars that must be paid to upgrade the gift to a unique one
 * @property totalCount Optional. The total number of gifts of this type that can be sent by all users; for limited gifts only
 * @property remainingCount Optional. The number of remaining gifts of this type that can be sent by all users; for limited gifts only
 * @property hasColors Optional. True, if the gift can be used (after being upgraded) to customize a user's appearance
 * @property personalTotalCount Optional. The total number of gifts of this type that can be sent by the bot; for limited gifts only
 * @property personalRemainingCount Optional. The number of remaining gifts of this type that can be sent by the bot; for limited gifts only
 * @property background Optional. Background of the gift
 * @property uniqueGiftVariantCount Optional. The total number of different unique gifts that can be obtained by upgrading the gift
 * @property publisherChat Optional. Information about the chat that published the gift
 */
@Serializable
data class Gift(
    val id: String,
    val sticker: Sticker,
    val starCount: Int,
    val totalCount: Int? = null,
    val remainingCount: Int? = null,
    val upgradeStarCount: Int? = null,
    val hasColors: Boolean? = null,
    val personalTotalCount: Int? = null,
    val personalRemainingCount: Int? = null,
    val background: GiftBackground? = null,
    val uniqueGiftVariantCount: Int? = null,
    val publisherChat: Chat? = null,
    override val isPremium: Boolean? = null,
) : IsPremiumProp {
}
