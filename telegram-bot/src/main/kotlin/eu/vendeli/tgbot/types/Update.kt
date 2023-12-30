package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.chat.ChatJoinRequest
import eu.vendeli.tgbot.types.chat.ChatMemberUpdated
import eu.vendeli.tgbot.types.inline.ChosenInlineResult
import eu.vendeli.tgbot.types.inline.InlineQuery
import eu.vendeli.tgbot.types.payment.PreCheckoutQuery
import eu.vendeli.tgbot.types.payment.ShippingQuery

data class Update(
    val updateId: Int,
    val message: Message? = null,
    val editedMessage: Message? = null,
    val channelPost: Message? = null,
    val editedChannelPost: Message? = null,
    val messageReaction: MessageReactionUpdated? = null,
    val messageReactionCount: MessageReactionCountUpdated? = null,
    val inlineQuery: InlineQuery? = null,
    val chosenInlineResult: ChosenInlineResult? = null,
    val callbackQuery: CallbackQuery? = null,
    val shippingQuery: ShippingQuery? = null,
    val preCheckoutQuery: PreCheckoutQuery? = null,
    val poll: Poll? = null,
    val pollAnswer: PollAnswer? = null,
    val myChatMember: ChatMemberUpdated? = null,
    val chatMember: ChatMemberUpdated? = null,
    val chatJoinRequest: ChatJoinRequest? = null,
) : MultipleResponse
