package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.CallbackQuery
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
import eu.vendeli.tgbot.types.media.PaidMediaPurchased
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.msg.MessageReactionCountUpdated
import eu.vendeli.tgbot.types.msg.MessageReactionUpdated
import eu.vendeli.tgbot.types.payment.PreCheckoutQuery
import eu.vendeli.tgbot.types.payment.ShippingQuery
import eu.vendeli.tgbot.types.poll.Poll
import eu.vendeli.tgbot.utils.serde.UpdateSerializer
import kotlinx.serialization.Serializable

interface UserReference {
    val user: User?
}

interface TextReference {
    val text: String
        get() = ""
}

@Serializable(with = ProcessedUpdate.Companion::class)
sealed class ProcessedUpdate(
    open val updateId: Int,
    open val origin: Update,
    internal val type: UpdateType,
) : TextReference,
    MultipleResponse {
    internal companion object : UpdateSerializer<ProcessedUpdate>()
}

@Serializable(MessageUpdate.Companion::class)
data class MessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val message: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.MESSAGE),
    UserReference {
    override val user = message.from!!
    override val text = message.text.orEmpty()

    internal companion object : UpdateSerializer<MessageUpdate>()
}

@Serializable(EditedMessageUpdate.Companion::class)
data class EditedMessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val editedMessage: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.EDIT_MESSAGE),
    UserReference {
    override val user = editedMessage.from!!
    override val text = editedMessage.text.orEmpty()

    internal companion object : UpdateSerializer<EditedMessageUpdate>()
}

@Serializable(ChannelPostUpdate.Companion::class)
data class ChannelPostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val channelPost: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.CHANNEL_POST),
    UserReference {
    override val user = channelPost.from
    override val text = channelPost.text.orEmpty()

    internal companion object : UpdateSerializer<ChannelPostUpdate>()
}

@Serializable(EditedChannelPostUpdate.Companion::class)
data class EditedChannelPostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val editedChannelPost: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.EDITED_CHANNEL_POST),
    UserReference {
    override val user = editedChannelPost.from
    override val text = editedChannelPost.text.orEmpty()

    internal companion object : UpdateSerializer<EditedChannelPostUpdate>()
}

@Serializable(BusinessConnectionUpdate.Companion::class)
data class BusinessConnectionUpdate(
    override val updateId: Int,
    override val origin: Update,
    val businessConnection: BusinessConnection,
) : ProcessedUpdate(updateId, origin, UpdateType.BUSINESS_CONNECTION),
    UserReference {
    override val user = businessConnection.user

    internal companion object : UpdateSerializer<BusinessConnectionUpdate>()
}

@Serializable(BusinessMessageUpdate.Companion::class)
data class BusinessMessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val businessMessage: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.BUSINESS_MESSAGE),
    UserReference {
    override val user = businessMessage.from
    override val text = businessMessage.text.orEmpty()

    internal companion object : UpdateSerializer<BusinessMessageUpdate>()
}

@Serializable(EditedBusinessMessageUpdate.Companion::class)
data class EditedBusinessMessageUpdate(
    override val updateId: Int,
    override val origin: Update,
    val editedBusinessMessage: Message,
) : ProcessedUpdate(updateId, origin, UpdateType.EDITED_BUSINESS_MESSAGE),
    UserReference {
    override val user = editedBusinessMessage.from
    override val text = editedBusinessMessage.text.orEmpty()

    internal companion object : UpdateSerializer<EditedBusinessMessageUpdate>()
}

@Serializable(DeletedBusinessMessagesUpdate.Companion::class)
data class DeletedBusinessMessagesUpdate(
    override val updateId: Int,
    override val origin: Update,
    val deletedBusinessMessages: BusinessMessagesDeleted,
) : ProcessedUpdate(updateId, origin, UpdateType.DELETED_BUSINESS_MESSAGES) {
    internal companion object : UpdateSerializer<DeletedBusinessMessagesUpdate>()
}

@Serializable(MessageReactionUpdate.Companion::class)
data class MessageReactionUpdate(
    override val updateId: Int,
    override val origin: Update,
    val messageReaction: MessageReactionUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.MESSAGE_REACTION),
    UserReference {
    override val user = messageReaction.user

    internal companion object : UpdateSerializer<MessageReactionUpdate>()
}

@Serializable(MessageReactionCountUpdate.Companion::class)
data class MessageReactionCountUpdate(
    override val updateId: Int,
    override val origin: Update,
    val messageReactionCount: MessageReactionCountUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.MESSAGE_REACTION_COUNT) {
    internal companion object : UpdateSerializer<MessageReactionCountUpdate>()
}

@Serializable(InlineQueryUpdate.Companion::class)
data class InlineQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val inlineQuery: InlineQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.INLINE_QUERY),
    UserReference {
    override val user: User = inlineQuery.from
    override val text = inlineQuery.query

    internal companion object : UpdateSerializer<InlineQueryUpdate>()
}

@Serializable(ChosenInlineResultUpdate.Companion::class)
data class ChosenInlineResultUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chosenInlineResult: ChosenInlineResult,
) : ProcessedUpdate(updateId, origin, UpdateType.CHOSEN_INLINE_RESULT),
    UserReference {
    override val user: User = chosenInlineResult.from
    override val text = chosenInlineResult.query

    internal companion object : UpdateSerializer<ChosenInlineResultUpdate>()
}

@Serializable(CallbackQueryUpdate.Companion::class)
data class CallbackQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val callbackQuery: CallbackQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.CALLBACK_QUERY),
    UserReference {
    override val user: User = callbackQuery.from
    override val text = callbackQuery.data.orEmpty()

    internal companion object : UpdateSerializer<CallbackQueryUpdate>()
}

@Serializable(ShippingQueryUpdate.Companion::class)
data class ShippingQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val shippingQuery: ShippingQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.SHIPPING_QUERY),
    UserReference {
    override val user: User = shippingQuery.from
    override val text = shippingQuery.invoicePayload

    internal companion object : UpdateSerializer<ShippingQueryUpdate>()
}

@Serializable(PreCheckoutQueryUpdate.Companion::class)
data class PreCheckoutQueryUpdate(
    override val updateId: Int,
    override val origin: Update,
    val preCheckoutQuery: PreCheckoutQuery,
) : ProcessedUpdate(updateId, origin, UpdateType.PRE_CHECKOUT_QUERY),
    UserReference {
    override val user: User = preCheckoutQuery.from
    override val text = preCheckoutQuery.invoicePayload

    internal companion object : UpdateSerializer<PreCheckoutQueryUpdate>()
}

@Serializable(PollUpdate.Companion::class)
data class PollUpdate(
    override val updateId: Int,
    override val origin: Update,
    val poll: Poll,
) : ProcessedUpdate(updateId, origin, UpdateType.POLL) {
    override val text = poll.question

    internal companion object : UpdateSerializer<PollUpdate>()
}

@Serializable(PollAnswerUpdate.Companion::class)
data class PollAnswerUpdate(
    override val updateId: Int,
    override val origin: Update,
    val pollAnswer: PollAnswer,
) : ProcessedUpdate(updateId, origin, UpdateType.POLL_ANSWER),
    UserReference {
    override val user: User? = pollAnswer.user

    internal companion object : UpdateSerializer<PollAnswerUpdate>()
}

@Serializable(MyChatMemberUpdate.Companion::class)
data class MyChatMemberUpdate(
    override val updateId: Int,
    override val origin: Update,
    val myChatMember: ChatMemberUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.MY_CHAT_MEMBER),
    UserReference {
    override val user: User = myChatMember.from

    internal companion object : UpdateSerializer<MyChatMemberUpdate>()
}

@Serializable(ChatMemberUpdate.Companion::class)
data class ChatMemberUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chatMember: ChatMemberUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.CHAT_MEMBER),
    UserReference {
    override val user: User = chatMember.from

    internal companion object : UpdateSerializer<ChatMemberUpdate>()
}

@Serializable(ChatJoinRequestUpdate.Companion::class)
data class ChatJoinRequestUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chatJoinRequest: ChatJoinRequest,
) : ProcessedUpdate(updateId, origin, UpdateType.CHAT_JOIN_REQUEST),
    UserReference {
    override val user: User = chatJoinRequest.from

    internal companion object : UpdateSerializer<ChatJoinRequestUpdate>()
}

@Serializable(ChatBoostUpdate.Companion::class)
data class ChatBoostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val chatBoost: ChatBoostUpdated,
) : ProcessedUpdate(updateId, origin, UpdateType.CHAT_BOOST) {
    internal companion object : UpdateSerializer<ChatBoostUpdate>()
}

@Serializable(RemovedChatBoostUpdate.Companion::class)
data class RemovedChatBoostUpdate(
    override val updateId: Int,
    override val origin: Update,
    val removedChatBoost: ChatBoostRemoved,
) : ProcessedUpdate(updateId, origin, UpdateType.REMOVED_CHAT_BOOST) {
    internal companion object : UpdateSerializer<RemovedChatBoostUpdate>()
}

@Serializable(PurchasedPaidMediaUpdate.Companion::class)
data class PurchasedPaidMediaUpdate(
    override val updateId: Int,
    override val origin: Update,
    val purchasedPaidMedia: PaidMediaPurchased,
) : ProcessedUpdate(updateId, origin, UpdateType.PURCHASED_PAID_MEDIA),
    UserReference,
    TextReference {
    override val user: User = purchasedPaidMedia.from
    override val text: String = purchasedPaidMedia.paidMediaPayload

    internal companion object : UpdateSerializer<PurchasedPaidMediaUpdate>()
}

inline val ProcessedUpdate.userOrNull: User? get() = (this as? UserReference)?.user

@Suppress("NOTHING_TO_INLINE")
inline fun ProcessedUpdate.getUser(): User = userOrNull ?: throw NullPointerException("User not found.")
