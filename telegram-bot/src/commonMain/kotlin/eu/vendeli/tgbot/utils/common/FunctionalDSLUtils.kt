package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.types.component.ActivityCtx
import eu.vendeli.tgbot.types.component.BusinessConnectionUpdate
import eu.vendeli.tgbot.types.component.BusinessMessageUpdate
import eu.vendeli.tgbot.types.component.CallbackQueryUpdate
import eu.vendeli.tgbot.types.component.ChannelPostUpdate
import eu.vendeli.tgbot.types.component.ChatBoostUpdate
import eu.vendeli.tgbot.types.component.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.component.ChatMemberUpdate
import eu.vendeli.tgbot.types.component.ChosenInlineResultUpdate
import eu.vendeli.tgbot.types.component.DeletedBusinessMessagesUpdate
import eu.vendeli.tgbot.types.component.EditedBusinessMessageUpdate
import eu.vendeli.tgbot.types.component.EditedChannelPostUpdate
import eu.vendeli.tgbot.types.component.EditedMessageUpdate
import eu.vendeli.tgbot.types.component.InlineQueryUpdate
import eu.vendeli.tgbot.types.component.MessageReactionCountUpdate
import eu.vendeli.tgbot.types.component.MessageReactionUpdate
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.component.MyChatMemberUpdate
import eu.vendeli.tgbot.types.component.PollAnswerUpdate
import eu.vendeli.tgbot.types.component.PollUpdate
import eu.vendeli.tgbot.types.component.PreCheckoutQueryUpdate
import eu.vendeli.tgbot.types.component.PurchasedPaidMediaUpdate
import eu.vendeli.tgbot.types.component.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.component.ShippingQueryUpdate
import eu.vendeli.tgbot.types.component.UpdateType
import kotlin.Unit

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.message] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMessage(block: suspend ActivityCtx<MessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.editedMessage] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onEditedMessage(block: suspend ActivityCtx<EditedMessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.EDITED_MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.channelPost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChannelPost(block: suspend ActivityCtx<ChannelPostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHANNEL_POST] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.editedChannelPost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onEditedChannelPost(block: suspend ActivityCtx<EditedChannelPostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.EDITED_CHANNEL_POST] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.businessConnection] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onBusinessConnection(block: suspend ActivityCtx<BusinessConnectionUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.BUSINESS_CONNECTION] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.businessMessage] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onBusinessMessage(block: suspend ActivityCtx<BusinessMessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.BUSINESS_MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.editedBusinessMessage] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onEditedBusinessMessage(block: suspend ActivityCtx<EditedBusinessMessageUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.EDITED_BUSINESS_MESSAGE] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.deletedBusinessMessages] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onDeletedBusinessMessages(block: suspend ActivityCtx<DeletedBusinessMessagesUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.DELETED_BUSINESS_MESSAGES] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.messageReaction] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMessageReaction(block: suspend ActivityCtx<MessageReactionUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MESSAGE_REACTION] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.messageReactionCount] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMessageReactionCount(block: suspend ActivityCtx<MessageReactionCountUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MESSAGE_REACTION_COUNT] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.inlineQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onInlineQuery(block: suspend ActivityCtx<InlineQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.INLINE_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chosenInlineResult] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChosenInlineResult(block: suspend ActivityCtx<ChosenInlineResultUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHOSEN_INLINE_RESULT] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.callbackQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onCallbackQuery(block: suspend ActivityCtx<CallbackQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CALLBACK_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.shippingQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onShippingQuery(block: suspend ActivityCtx<ShippingQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.SHIPPING_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.preCheckoutQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPreCheckoutQuery(block: suspend ActivityCtx<PreCheckoutQueryUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.PRE_CHECKOUT_QUERY] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.poll] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPoll(block: suspend ActivityCtx<PollUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.POLL] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.pollAnswer] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPollAnswer(block: suspend ActivityCtx<PollAnswerUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.POLL_ANSWER] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.myChatMember] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMyChatMember(block: suspend ActivityCtx<MyChatMemberUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.MY_CHAT_MEMBER] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chatMember] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChatMember(block: suspend ActivityCtx<ChatMemberUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHAT_MEMBER] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chatJoinRequest] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChatJoinRequest(block: suspend ActivityCtx<ChatJoinRequestUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHAT_JOIN_REQUEST] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chatBoost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChatBoost(block: suspend ActivityCtx<ChatBoostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.CHAT_BOOST] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.removedChatBoost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onRemovedChatBoost(block: suspend ActivityCtx<RemovedChatBoostUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.REMOVED_CHAT_BOOST] = block.cast()
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.purchasedPaidMedia] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPurchasedPaidMedia(block: suspend ActivityCtx<PurchasedPaidMediaUpdate>.() -> Unit) {
  functionalActivities.onUpdateActivities[UpdateType.PURCHASED_PAID_MEDIA] = block.cast()
}
