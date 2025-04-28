package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * Contains the list of gifts received and owned by a user or a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#ownedgifts)
 * @property totalCount The total number of gifts owned by the user or the chat
 * @property gifts The list of gifts
 * @property nextOffset Optional. Offset for the next request. If empty, then there are no more results
 */
@Serializable
data class OwnedGifts(
    val totalCount: Int,
    val gifts: List<OwnedGift>,
    val nextOffset: String? = null,
)
