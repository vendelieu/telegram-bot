package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.CallbackQuery
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.MessageReactionCountUpdated
import eu.vendeli.tgbot.types.MessageReactionUpdated
import eu.vendeli.tgbot.types.PollAnswer
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.boost.ChatBoostRemoved
import eu.vendeli.tgbot.types.boost.ChatBoostUpdated
import eu.vendeli.tgbot.types.business.BusinessConnection
import eu.vendeli.tgbot.types.business.BusinessMessagesDeleted
import eu.vendeli.tgbot.types.chat.ChatJoinRequest
import eu.vendeli.tgbot.types.chat.ChatMemberUpdated
import eu.vendeli.tgbot.types.inline.ChosenInlineResult
import eu.vendeli.tgbot.types.inline.InlineQuery
import eu.vendeli.tgbot.types.payment.PreCheckoutQuery
import eu.vendeli.tgbot.types.payment.ShippingQuery
import eu.vendeli.tgbot.types.poll.Poll

interface UserReference {
    val user: User?
}

interface TextReference {
    val text: String
        get() = ""
}

sealed class ProcessedUpdate(
    open val updateId: Int,
    open val origin: Update,
    internal val type: UpdateType,
) : TextReference {
    val update: Update get() = origin
}

data class MessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val message: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.MESSAGE),
    UserReference {
    override val user = message.from!!
    override val text = message.text.orEmpty()
}

data class EditedMessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val editedMessage: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.EDIT_MESSAGE),
    UserReference {
    override val user = editedMessage.from!!
    override val text = editedMessage.text.orEmpty()
}

data class ChannelPostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val channelPost: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.CHANNEL_POST),
    UserReference {
    override val user = channelPost.from
    override val text = channelPost.text.orEmpty()
}

data class EditedChannelPostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val editedChannelPost: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.EDITED_CHANNEL_POST),
    UserReference {
    override val user = editedChannelPost.from
    override val text = editedChannelPost.text.orEmpty()
}

data class BusinessConnectionUpdate(
    override val updateId: Int,
    override val origin: Update,
    val businessConnection: BusinessConnection,
) : ProcessedUpdate(updateId, origin, UpdateType.BUSINESS_CONNECTION),
    UserReference {
    override val user = businessConnection.user
}

data class BusinessMessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val businessMessage: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.BUSINESS_MESSAGE),
    UserReference {
    override val user = businessMessage.from
    override val text = businessMessage.text.orEmpty()
}

data class EditedBusinessMessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val editedBusinessMessage: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.EDITED_BUSINESS_MESSAGE),
    UserReference {
    override val user = editedBusinessMessage.from
    override val text = editedBusinessMessage.text.orEmpty()
}

data class DeletedBusinessMessagesUpdate(
    override val updateId: Int,
    override val origin: Update,
    val deletedBusinessMessages: BusinessMessagesDeleted,
) : ProcessedUpdate(updateId, origin, UpdateType.DELETED_BUSINESS_MESSAGES)

data class MessageReactionUpdate(
    override val updateId: Int,
    override val origin: Update,
    val messageReaction: MessageReactionUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.MESSAGE_REACTION),
    UserReference {
    override val user = messageReaction.user
}

data class MessageReactionCountUpdate(
    override val updateId: Int,
    override val origin: Update,
    val messageReactionCount: MessageReactionCountUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.MESSAGE_REACTION_COUNT)

data class InlineQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val inlineQuery: InlineQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.INLINE_QUERY),
    UserReference {
    override val user: User = inlineQuery.from
    override val text = inlineQuery.query
}

data class ChosenInlineResultUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chosenInlineResult: ChosenInlineResult,
) : ProcessedUpdate(updateId, origin, UpdateType.CHOSEN_INLINE_RESULT),
    UserReference {
    override val user: User = chosenInlineResult.from
    override val text = chosenInlineResult.query
}

data class CallbackQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val callbackQuery: CallbackQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.CALLBACK_QUERY),
    UserReference {
    override val user: User = callbackQuery.from
    override val text = callbackQuery.data.orEmpty()
}

data class ShippingQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val shippingQuery: ShippingQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.SHIPPING_QUERY),
    UserReference {
    override val user: User = shippingQuery.from
    override val text = shippingQuery.invoicePayload
}

data class PreCheckoutQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val preCheckoutQuery: PreCheckoutQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.PRE_CHECKOUT_QUERY),
    UserReference {
    override val user: User = preCheckoutQuery.from
    override val text = preCheckoutQuery.invoicePayload
}

data class PollUpdate(
    override val updateId: Int,
    override val origin: Update,
    val poll: Poll,
) : ProcessedUpdate(updateId, origin, UpdateType.POLL) {
    override val text = poll.question
}

data class PollAnswerUpdate(
    override val updateId: Int,
    override val origin: Update,
    val pollAnswer: PollAnswer,
) : ProcessedUpdate(updateId, origin, UpdateType.POLL_ANSWER),
    UserReference {
    override val user: User = pollAnswer.user
}

data class MyChatMemberUpdate(
    override val updateId: Int,
    override val origin: Update,
    val myChatMember: ChatMemberUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.MY_CHAT_MEMBER),
    UserReference {
    override val user: User = myChatMember.from
}

data class ChatMemberUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chatMember: ChatMemberUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.CHAT_MEMBER),
    UserReference {
    override val user: User = chatMember.from
}

data class ChatJoinRequestUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chatJoinRequest: ChatJoinRequest,
) : ProcessedUpdate(updateId, origin, UpdateType.CHAT_JOIN_REQUEST),
    UserReference {
    override val user: User = chatJoinRequest.from
}

data class ChatBoostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chatBoost: ChatBoostUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.CHAT_BOOST)

data class RemovedChatBoostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val removedChatBoost: ChatBoostRemoved,
) : ProcessedUpdate(updateId, origin, UpdateType.REMOVED_CHAT_BOOST)

inline val ProcessedUpdate.userOrNull: User? get() = (this as? UserReference)?.user

@Suppress("NOTHING_TO_INLINE")
inline fun ProcessedUpdate.getUser(): User = (this as? UserReference)?.user
    ?: throw NullPointerException("User not found.")
