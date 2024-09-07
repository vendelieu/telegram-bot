package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.boost.ChatBoostRemoved
import eu.vendeli.tgbot.types.boost.ChatBoostUpdated
import eu.vendeli.tgbot.types.business.BusinessConnection
import eu.vendeli.tgbot.types.business.BusinessMessagesDeleted
import eu.vendeli.tgbot.types.chat.ChatJoinRequest
import eu.vendeli.tgbot.types.chat.ChatMemberUpdated
import eu.vendeli.tgbot.types.inline.ChosenInlineResult
import eu.vendeli.tgbot.types.inline.InlineQuery
import eu.vendeli.tgbot.types.media.PaidMediaPurchased
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.msg.MessageReactionCountUpdated
import eu.vendeli.tgbot.types.msg.MessageReactionUpdated
import eu.vendeli.tgbot.types.payment.PreCheckoutQuery
import eu.vendeli.tgbot.types.payment.ShippingQuery
import eu.vendeli.tgbot.types.poll.Poll
import kotlinx.serialization.Serializable

/**
 * This object represents an incoming update.
 * At most one of the optional parameters can be present in any given update.
 *
 * [Api reference](https://core.telegram.org/bots/api#update)
 * @property updateId The update's unique identifier. Update identifiers start from a certain positive number and increase sequentially. This identifier becomes especially handy if you're using webhooks, since it allows you to ignore repeated updates or to restore the correct update sequence, should they get out of order. If there are no new updates for at least a week, then identifier of the next update will be chosen randomly instead of sequentially.
 * @property message Optional. New incoming message of any kind - text, photo, sticker, etc.
 * @property editedMessage Optional. New version of a message that is known to the bot and was edited. This update may at times be triggered by changes to message fields that are either unavailable or not actively used by your bot.
 * @property channelPost Optional. New incoming channel post of any kind - text, photo, sticker, etc.
 * @property editedChannelPost Optional. New version of a channel post that is known to the bot and was edited. This update may at times be triggered by changes to message fields that are either unavailable or not actively used by your bot.
 * @property businessConnection Optional. The bot was connected to or disconnected from a business account, or a user edited an existing connection with the bot
 * @property businessMessage Optional. New message from a connected business account
 * @property editedBusinessMessage Optional. New version of a message from a connected business account
 * @property deletedBusinessMessages Optional. Messages were deleted from a connected business account
 * @property messageReaction Optional. A reaction to a message was changed by a user. The bot must be an administrator in the chat and must explicitly specify "message_reaction" in the list of allowed_updates to receive these updates. The update isn't received for reactions set by bots.
 * @property messageReactionCount Optional. Reactions to a message with anonymous reactions were changed. The bot must be an administrator in the chat and must explicitly specify "message_reaction_count" in the list of allowed_updates to receive these updates. The updates are grouped and can be sent with delay up to a few minutes.
 * @property inlineQuery Optional. New incoming inline query
 * @property chosenInlineResult Optional. The result of an inline query that was chosen by a user and sent to their chat partner. Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
 * @property callbackQuery Optional. New incoming callback query
 * @property shippingQuery Optional. New incoming shipping query. Only for invoices with flexible price
 * @property preCheckoutQuery Optional. New incoming pre-checkout query. Contains full information about checkout
 * @property poll Optional. New poll state. Bots receive only updates about manually stopped polls and polls, which are sent by the bot
 * @property pollAnswer Optional. A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself.
 * @property myChatMember Optional. The bot's chat member status was updated in a chat. For private chats, this update is received only when the bot is blocked or unblocked by the user.
 * @property chatMember Optional. A chat member's status was updated in a chat. The bot must be an administrator in the chat and must explicitly specify "chat_member" in the list of allowed_updates to receive these updates.
 * @property chatJoinRequest Optional. A request to join the chat has been sent. The bot must have the can_invite_users administrator right in the chat to receive these updates.
 * @property chatBoost Optional. A chat boost was added or changed. The bot must be an administrator in the chat to receive these updates.
 * @property removedChatBoost Optional. A boost was removed from a chat. The bot must be an administrator in the chat to receive these updates.
 */
@Serializable
data class Update(
    val updateId: Int,
    val message: Message? = null,
    val editedMessage: Message? = null,
    val channelPost: Message? = null,
    val editedChannelPost: Message? = null,
    val businessConnection: BusinessConnection? = null,
    val businessMessage: Message? = null,
    val editedBusinessMessage: Message? = null,
    val deletedBusinessMessages: BusinessMessagesDeleted? = null,
    val messageReaction: MessageReactionUpdated? = null,
    val messageReactionCount: MessageReactionCountUpdated? = null,
    val inlineQuery: InlineQuery? = null,
    val chosenInlineResult: ChosenInlineResult? = null,
    val callbackQuery: CallbackQuery? = null,
    val shippingQuery: ShippingQuery? = null,
    val preCheckoutQuery: PreCheckoutQuery? = null,
    val purchasedPaidMedia: PaidMediaPurchased? = null,
    val poll: Poll? = null,
    val pollAnswer: PollAnswer? = null,
    val myChatMember: ChatMemberUpdated? = null,
    val chatMember: ChatMemberUpdated? = null,
    val chatJoinRequest: ChatJoinRequest? = null,
    val chatBoost: ChatBoostUpdated? = null,
    val removedChatBoost: ChatBoostRemoved? = null,
) : MultipleResponse
