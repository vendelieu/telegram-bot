package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a change in the price of direct messages sent to a channel chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#directmessagepricechanged)
 * @property areDirectMessagesEnabled True, if direct messages are enabled for the channel chat; false otherwise
 * @property directMessageStarCount Optional. The new number of Telegram Stars that must be paid by users for each direct message sent to the channel. Does not apply to users who have been exempted by administrators. Defaults to 0.
 */
@Serializable
data class DirectMessagePriceChanged(
    val areDirectMessagesEnabled: Boolean,
    val directMessageStarCount: Int = 0,
)
