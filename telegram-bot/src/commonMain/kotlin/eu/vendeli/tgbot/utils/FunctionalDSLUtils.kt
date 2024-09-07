package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.types.internal.ActivityCtx
import eu.vendeli.tgbot.types.internal.BusinessConnectionUpdate
import eu.vendeli.tgbot.types.internal.BusinessMessageUpdate
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.ChannelPostUpdate
import eu.vendeli.tgbot.types.internal.ChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.internal.ChatMemberUpdate
import eu.vendeli.tgbot.types.internal.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.internal.DeletedBusinessMessagesUpdate
import eu.vendeli.tgbot.types.internal.EditedBusinessMessageUpdate
import eu.vendeli.tgbot.types.internal.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.internal.EditedMessageUpdate
import eu.vendeli.tgbot.types.internal.InlineQueryUpdate
import eu.vendeli.tgbot.types.internal.MessageReactionCountUpdate
import eu.vendeli.tgbot.types.internal.MessageReactionUpdate
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.MyChatMemberUpdate
import eu.vendeli.tgbot.types.internal.PollAnswerUpdate
import eu.vendeli.tgbot.types.internal.PollUpdate
import eu.vendeli.tgbot.types.internal.PreCheckoutQueryUpdate
import eu.vendeli.tgbot.types.internal.PurchasedPaidMediaUpdate
import eu.vendeli.tgbot.types.internal.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate
import eu.vendeli.tgbot.types.internal.UpdateType

/**
 * Action that is performed on the presence of Message in the Update.
 */
public fun FunctionalHandlingDsl.onMessage(block: suspend ActivityCtx<MessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of EditedMessage in the Update.
 */
public
    fun FunctionalHandlingDsl.onEditedMessage(block: suspend ActivityCtx<EditedMessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.EDIT_MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of ChannelPost in the Update.
 */
public
    fun FunctionalHandlingDsl.onChannelPost(block: suspend ActivityCtx<ChannelPostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHANNEL_POST] = block.cast()
}

/**
 * Action that is performed on the presence of EditedChannelPost in the Update.
 */
public
    fun FunctionalHandlingDsl.onEditedChannelPost(block: suspend ActivityCtx<EditedChannelPostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.EDITED_CHANNEL_POST] = block.cast()
}

/**
 * Action that is performed on the presence of BusinessConnection in the Update.
 */
public
    fun FunctionalHandlingDsl.onBusinessConnection(block: suspend ActivityCtx<BusinessConnectionUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.BUSINESS_CONNECTION] = block.cast()
}

/**
 * Action that is performed on the presence of BusinessMessage in the Update.
 */
public
    fun FunctionalHandlingDsl.onBusinessMessage(block: suspend ActivityCtx<BusinessMessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.BUSINESS_MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of EditedBusinessMessage in the Update.
 */
public
    fun FunctionalHandlingDsl.onEditedBusinessMessage(block: suspend ActivityCtx<EditedBusinessMessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.EDITED_BUSINESS_MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of DeletedBusinessMessages in the Update.
 */
public
    fun FunctionalHandlingDsl.onDeletedBusinessMessages(block: suspend ActivityCtx<DeletedBusinessMessagesUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.DELETED_BUSINESS_MESSAGES] = block.cast()
}

/**
 * Action that is performed on the presence of MessageReaction in the Update.
 */
public
    fun FunctionalHandlingDsl.onMessageReaction(block: suspend ActivityCtx<MessageReactionUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MESSAGE_REACTION] = block.cast()
}

/**
 * Action that is performed on the presence of MessageReactionCount in the Update.
 */
public
    fun FunctionalHandlingDsl.onMessageReactionCount(block: suspend ActivityCtx<MessageReactionCountUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MESSAGE_REACTION_COUNT] = block.cast()
}

/**
 * Action that is performed on the presence of InlineQuery in the Update.
 */
public
    fun FunctionalHandlingDsl.onInlineQuery(block: suspend ActivityCtx<InlineQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.INLINE_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of ChosenInlineResult in the Update.
 */
public
    fun FunctionalHandlingDsl.onChosenInlineResult(block: suspend ActivityCtx<ChosenInlineResultUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHOSEN_INLINE_RESULT] = block.cast()
}

/**
 * Action that is performed on the presence of CallbackQuery in the Update.
 */
public
    fun FunctionalHandlingDsl.onCallbackQuery(block: suspend ActivityCtx<CallbackQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CALLBACK_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of ShippingQuery in the Update.
 */
public
    fun FunctionalHandlingDsl.onShippingQuery(block: suspend ActivityCtx<ShippingQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.SHIPPING_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of PreCheckoutQuery in the Update.
 */
public
    fun FunctionalHandlingDsl.onPreCheckoutQuery(block: suspend ActivityCtx<PreCheckoutQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.PRE_CHECKOUT_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of Poll in the Update.
 */
public fun FunctionalHandlingDsl.onPoll(block: suspend ActivityCtx<PollUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.POLL] = block.cast()
}

/**
 * Action that is performed on the presence of PollAnswer in the Update.
 */
public
    fun FunctionalHandlingDsl.onPollAnswer(block: suspend ActivityCtx<PollAnswerUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.POLL_ANSWER] = block.cast()
}

/**
 * Action that is performed on the presence of MyChatMember in the Update.
 */
public
    fun FunctionalHandlingDsl.onMyChatMember(block: suspend ActivityCtx<MyChatMemberUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MY_CHAT_MEMBER] = block.cast()
}

/**
 * Action that is performed on the presence of ChatMember in the Update.
 */
public
    fun FunctionalHandlingDsl.onChatMember(block: suspend ActivityCtx<ChatMemberUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHAT_MEMBER] = block.cast()
}

/**
 * Action that is performed on the presence of ChatJoinRequest in the Update.
 */
public
    fun FunctionalHandlingDsl.onChatJoinRequest(block: suspend ActivityCtx<ChatJoinRequestUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHAT_JOIN_REQUEST] = block.cast()
}

/**
 * Action that is performed on the presence of ChatBoost in the Update.
 */
public
    fun FunctionalHandlingDsl.onChatBoost(block: suspend ActivityCtx<ChatBoostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHAT_BOOST] = block.cast()
}

/**
 * Action that is performed on the presence of RemovedChatBoost in the Update.
 */
public
    fun FunctionalHandlingDsl.onRemovedChatBoost(block: suspend ActivityCtx<RemovedChatBoostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.REMOVED_CHAT_BOOST] = block.cast()
}

/**
 * Action that is performed on the presence of PurchasedPaidMedia in the Update.
 */
public
    fun FunctionalHandlingDsl.onPurchasedPaidMedia(block: suspend ActivityCtx<PurchasedPaidMediaUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.PURCHASED_PAID_MEDIA] = block.cast()
}
