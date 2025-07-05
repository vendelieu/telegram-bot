package eu.vendeli.tgbot.types.giveaway

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents a message about a scheduled giveaway.
 *
 * [Api reference](https://core.telegram.org/bots/api#giveaway)
 * @property chats The list of chats which the user must join to participate in the giveaway
 * @property winnersSelectionDate Point in time (Unix timestamp) when winners of the giveaway will be selected
 * @property winnerCount The number of users which are supposed to be selected as winners of the giveaway
 * @property onlyNewMembers Optional. True, if only users who join the chats after the giveaway started should be eligible to win
 * @property hasPublicWinners Optional. True, if the list of giveaway winners will be visible to everyone
 * @property prizeDescription Optional. Description of additional giveaway prize
 * @property countryCodes Optional. A list of two-letter ISO 3166-1 alpha-2 country codes indicating the countries from which eligible users for the giveaway must come. If empty, then all users can participate in the giveaway. Users with a phone number that was bought on Fragment can always participate in giveaways.
 * @property prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 * @property premiumSubscriptionMonthCount Optional. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
 */
@Serializable
data class Giveaway(
    val chats: List<Chat>,
    @Serializable(InstantSerializer::class)
    val winnersSelectionDate: Instant,
    val winnerCount: Int,
    val onlyNewMembers: Boolean? = null,
    val hasPublicWinners: Boolean? = null,
    val prizeDescription: String? = null,
    val countryCodes: List<String>? = null,
    val prizeStarCount: Int? = null,
    val premiumSubscriptionMonthCount: Int? = null,
)
