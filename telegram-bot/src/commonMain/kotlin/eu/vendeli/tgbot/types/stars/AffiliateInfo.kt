package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * Contains information about the affiliate that received a commission via this transaction.
 *
 * [Api reference](https://core.telegram.org/bots/api#affiliateinfo)
 * @property affiliateUser Optional. The bot or the user that received an affiliate commission if it was received by a bot or a user
 * @property affiliateChat Optional. The chat that received an affiliate commission if it was received by a chat
 * @property commissionPerMille The number of Telegram Stars received by the affiliate for each 1000 Telegram Stars received by the bot from referred users
 * @property amount Integer amount of Telegram Stars received by the affiliate from the transaction, rounded to 0; can be negative for refunds
 * @property nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars received by the affiliate; from -999999999 to 999999999; can be negative for refunds
 */
@Serializable
data class AffiliateInfo(
    val affiliateUser: User? = null,
    val affiliateChat: Chat? = null,
    val commissionPerMille: Int,
    val amount: Int,
    val nanostarAmount: Int? = null,
)
