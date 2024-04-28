package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * This object represents a message about the completion of a giveaway with public winners.
 *
 * [Api reference](https://core.telegram.org/bots/api#giveawaywinners)
 * @property chat The chat that created the giveaway
 * @property giveawayMessageId Identifier of the message with the giveaway in the chat
 * @property winnersSelectionDate Point in time (Unix timestamp) when winners of the giveaway were selected
 * @property winnerCount Total number of winners in the giveaway
 * @property winners List of up to 100 winners of the giveaway
 * @property additionalChatCount Optional. The number of other chats the user had to join in order to be eligible for the giveaway
 * @property premiumSubscriptionMonthCount Optional. The number of months the Telegram Premium subscription won from the giveaway will be active for
 * @property unclaimedPrizeCount Optional. Number of undistributed prizes
 * @property onlyNewMembers Optional. True, if only users who had joined the chats after the giveaway started were eligible to win
 * @property wasRefunded Optional. True, if the giveaway was canceled because the payment for it was refunded
 * @property prizeDescription Optional. Description of additional giveaway prize
 */
@Serializable
data class GiveawayWinners(
    val chat: Chat,
    val giveawayMessageId: Long,
    val winnersSelectionDate: Long,
    val winnerCount: Int,
    val winners: List<User>,
    val additionalChatCount: Int? = null,
    val premiumSubscriptionMonthCount: Int? = null,
    val unclaimedPrizeCount: Int? = null,
    val onlyNewMembers: Boolean? = null,
    val wasRefunded: Boolean? = null,
    val prizeDescription: String? = null,
)
