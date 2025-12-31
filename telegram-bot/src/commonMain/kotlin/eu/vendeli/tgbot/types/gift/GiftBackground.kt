package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * This object describes the background of a gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#giftbackground)
 * @property centerColor Center color of the background in RGB format
 * @property edgeColor Edge color of the background in RGB format
 * @property textColor Text color of the background in RGB format
 */
@Serializable
data class GiftBackground(
    val centerColor: Int,
    val edgeColor: Int,
    val textColor: Int,
)

