package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.CallbackQuery
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.MessageReactionCountUpdated
import eu.vendeli.tgbot.types.MessageReactionUpdated
import eu.vendeli.tgbot.types.Poll
import eu.vendeli.tgbot.types.PollAnswer
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.ChatJoinRequest
import eu.vendeli.tgbot.types.chat.ChatMemberUpdated
import eu.vendeli.tgbot.types.inline.ChosenInlineResult
import eu.vendeli.tgbot.types.inline.InlineQuery
import eu.vendeli.tgbot.types.payment.PreCheckoutQuery
import eu.vendeli.tgbot.types.payment.ShippingQuery

interface UserReference {
    val user: User?
}

interface TextReference {
    val text: String
        get() = ""
}

sealed class ProcessedUpdate(
    open val updateId: Int,
    open val update: Update,
    internal val type: UpdateType,
) : TextReference

data class MessageUpdate(
    override val updateId: Int,
    override val update: Update,
    val message: Message,
) : ProcessedUpdate(updateId, update, UpdateType.MESSAGE), UserReference {
    override val user = message.from!!
    override val text = message.text.orEmpty()
}

data class EditedMessageUpdate(
    override val updateId: Int,
    override val update: Update,
    val editedMessage: Message,
) : ProcessedUpdate(updateId, update, UpdateType.EDIT_MESSAGE), UserReference {
    override val user = editedMessage.from!!
    override val text = editedMessage.text.orEmpty()
}

data class ChannelPostUpdate(
    override val updateId: Int,
    override val update: Update,
    val channelPost: Message,
) : ProcessedUpdate(updateId, update, UpdateType.CHANNEL_POST), UserReference {
    override val user = channelPost.from
    override val text = channelPost.text.orEmpty()
}

data class EditedChannelPostUpdate(
    override val updateId: Int,
    override val update: Update,
    val editedChannelPost: Message,
) : ProcessedUpdate(updateId, update, UpdateType.EDITED_CHANNEL_POST), UserReference {
    override val user = editedChannelPost.from
    override val text = editedChannelPost.text.orEmpty()
}

data class MessageReactionUpdate(
    override val updateId: Int,
    override val update: Update,
    val messageReaction: MessageReactionUpdated,
) : ProcessedUpdate(updateId, update, UpdateType.MESSAGE_REACTION), UserReference {
    override val user = messageReaction.user
}

data class MessageReactionCountUpdate(
    override val updateId: Int,
    override val update: Update,
    val messageReactionCount: MessageReactionCountUpdated,
) : ProcessedUpdate(updateId, update, UpdateType.MESSAGE_REACTION_COUNT)

data class InlineQueryUpdate(
    override val updateId: Int,
    override val update: Update,
    val inlineQuery: InlineQuery,
) : ProcessedUpdate(updateId, update, UpdateType.INLINE_QUERY), UserReference {
    override val user: User = inlineQuery.from
    override val text = inlineQuery.query
}

data class ChosenInlineResultUpdate(
    override val updateId: Int,
    override val update: Update,
    val chosenInlineResult: ChosenInlineResult,
) : ProcessedUpdate(updateId, update, UpdateType.CHOSEN_INLINE_RESULT), UserReference {
    override val user: User = chosenInlineResult.from
    override val text = chosenInlineResult.query
}

data class CallbackQueryUpdate(
    override val updateId: Int,
    override val update: Update,
    val callbackQuery: CallbackQuery,
) : ProcessedUpdate(updateId, update, UpdateType.CALLBACK_QUERY), UserReference {
    override val user: User = callbackQuery.from
    override val text = callbackQuery.data.orEmpty()
}

data class ShippingQueryUpdate(
    override val updateId: Int,
    override val update: Update,
    val shippingQuery: ShippingQuery,
) : ProcessedUpdate(updateId, update, UpdateType.SHIPPING_QUERY), UserReference {
    override val user: User = shippingQuery.from
    override val text = shippingQuery.invoicePayload
}

data class PreCheckoutQueryUpdate(
    override val updateId: Int,
    override val update: Update,
    val preCheckoutQuery: PreCheckoutQuery,
) : ProcessedUpdate(updateId, update, UpdateType.PRE_CHECKOUT_QUERY), UserReference {
    override val user: User = preCheckoutQuery.from
    override val text = preCheckoutQuery.invoicePayload
}

data class PollUpdate(
    override val updateId: Int,
    override val update: Update,
    val poll: Poll,
) : ProcessedUpdate(updateId, update, UpdateType.POLL) {
    override val text = poll.question
}

data class PollAnswerUpdate(
    override val updateId: Int,
    override val update: Update,
    val pollAnswer: PollAnswer,
) : ProcessedUpdate(updateId, update, UpdateType.POLL_ANSWER), UserReference {
    override val user: User = pollAnswer.user
}

data class MyChatMemberUpdate(
    override val updateId: Int,
    override val update: Update,
    val myChatMember: ChatMemberUpdated,
) : ProcessedUpdate(updateId, update, UpdateType.MY_CHAT_MEMBER), UserReference {
    override val user: User = myChatMember.from
}

data class ChatMemberUpdate(
    override val updateId: Int,
    override val update: Update,
    val chatMember: ChatMemberUpdated,
) : ProcessedUpdate(updateId, update, UpdateType.CHAT_MEMBER), UserReference {
    override val user: User = chatMember.from
}

data class ChatJoinRequestUpdate(
    override val updateId: Int,
    override val update: Update,
    val chatJoinRequest: ChatJoinRequest,
) : ProcessedUpdate(updateId, update, UpdateType.CHAT_JOIN_REQUEST), UserReference {
    override val user: User = chatJoinRequest.from
}

inline val ProcessedUpdate.userOrNull: User? get() = (this as? UserReference)?.user

@Suppress("NOTHING_TO_INLINE")
inline fun ProcessedUpdate.getUser(): User = (this as? UserReference)?.user
    ?: throw NullPointerException("User not found.")
