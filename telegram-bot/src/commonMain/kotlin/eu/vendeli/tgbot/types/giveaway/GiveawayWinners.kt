package eu.vendeli.tgbot.types.giveaway

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
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
 * @property prizeStarCount Optional. The number of Telegram Stars that were split between giveaway winners; for Telegram Star giveaways only
 * @property premiumSubscriptionMonthCount Optional. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
 * @property unclaimedPrizeCount Optional. Number of undistributed prizes
 * @property onlyNewMembers Optional. True, if only users who had joined the chats after the giveaway started were eligible to win
 * @property wasRefunded Optional. True, if the giveaway was canceled because the payment for it was refunded
 * @property prizeDescription Optional. Description of additional giveaway prize
 */
@Serializable
data class GiveawayWinners(
    val chat: Chat,
    val giveawayMessageId: Long,
    @Serializable(InstantSerializer::class)
    val winnersSelectionDate: Instant,
    val winnerCount: Int,
    val winners: List<User>,
    val additionalChatCount: Int? = null,
    val prizeStarCount: Int? = null,
    val premiumSubscriptionMonthCount: Int? = null,
    val unclaimedPrizeCount: Int? = null,
    val onlyNewMembers: Boolean? = null,
    val wasRefunded: Boolean? = null,
    val prizeDescription: String? = null,
)
