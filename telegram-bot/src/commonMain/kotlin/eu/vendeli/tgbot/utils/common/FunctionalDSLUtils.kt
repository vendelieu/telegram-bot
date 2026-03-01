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
  onUpdate(UpdateType.MESSAGE) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<MessageUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.editedMessage] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onEditedMessage(block: suspend ActivityCtx<EditedMessageUpdate>.() -> Unit) {
  onUpdate(UpdateType.EDITED_MESSAGE) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<EditedMessageUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.channelPost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChannelPost(block: suspend ActivityCtx<ChannelPostUpdate>.() -> Unit) {
  onUpdate(UpdateType.CHANNEL_POST) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<ChannelPostUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.editedChannelPost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onEditedChannelPost(block: suspend ActivityCtx<EditedChannelPostUpdate>.() -> Unit) {
  onUpdate(UpdateType.EDITED_CHANNEL_POST) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<EditedChannelPostUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.businessConnection] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onBusinessConnection(block: suspend ActivityCtx<BusinessConnectionUpdate>.() -> Unit) {
  onUpdate(UpdateType.BUSINESS_CONNECTION) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<BusinessConnectionUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.businessMessage] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onBusinessMessage(block: suspend ActivityCtx<BusinessMessageUpdate>.() -> Unit) {
  onUpdate(UpdateType.BUSINESS_MESSAGE) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<BusinessMessageUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.editedBusinessMessage] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onEditedBusinessMessage(block: suspend ActivityCtx<EditedBusinessMessageUpdate>.() -> Unit) {
  onUpdate(UpdateType.EDITED_BUSINESS_MESSAGE) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<EditedBusinessMessageUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.deletedBusinessMessages] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onDeletedBusinessMessages(block: suspend ActivityCtx<DeletedBusinessMessagesUpdate>.() -> Unit) {
  onUpdate(UpdateType.DELETED_BUSINESS_MESSAGES) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<DeletedBusinessMessagesUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.messageReaction] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMessageReaction(block: suspend ActivityCtx<MessageReactionUpdate>.() -> Unit) {
  onUpdate(UpdateType.MESSAGE_REACTION) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<MessageReactionUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.messageReactionCount] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMessageReactionCount(block: suspend ActivityCtx<MessageReactionCountUpdate>.() -> Unit) {
  onUpdate(UpdateType.MESSAGE_REACTION_COUNT) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<MessageReactionCountUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.inlineQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onInlineQuery(block: suspend ActivityCtx<InlineQueryUpdate>.() -> Unit) {
  onUpdate(UpdateType.INLINE_QUERY) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<InlineQueryUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chosenInlineResult] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChosenInlineResult(block: suspend ActivityCtx<ChosenInlineResultUpdate>.() -> Unit) {
  onUpdate(UpdateType.CHOSEN_INLINE_RESULT) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<ChosenInlineResultUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.callbackQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onCallbackQuery(block: suspend ActivityCtx<CallbackQueryUpdate>.() -> Unit) {
  onUpdate(UpdateType.CALLBACK_QUERY) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<CallbackQueryUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.shippingQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onShippingQuery(block: suspend ActivityCtx<ShippingQueryUpdate>.() -> Unit) {
  onUpdate(UpdateType.SHIPPING_QUERY) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<ShippingQueryUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.preCheckoutQuery] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPreCheckoutQuery(block: suspend ActivityCtx<PreCheckoutQueryUpdate>.() -> Unit) {
  onUpdate(UpdateType.PRE_CHECKOUT_QUERY) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<PreCheckoutQueryUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.poll] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPoll(block: suspend ActivityCtx<PollUpdate>.() -> Unit) {
  onUpdate(UpdateType.POLL) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<PollUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.pollAnswer] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPollAnswer(block: suspend ActivityCtx<PollAnswerUpdate>.() -> Unit) {
  onUpdate(UpdateType.POLL_ANSWER) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<PollAnswerUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.myChatMember] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onMyChatMember(block: suspend ActivityCtx<MyChatMemberUpdate>.() -> Unit) {
  onUpdate(UpdateType.MY_CHAT_MEMBER) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<MyChatMemberUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chatMember] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChatMember(block: suspend ActivityCtx<ChatMemberUpdate>.() -> Unit) {
  onUpdate(UpdateType.CHAT_MEMBER) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<ChatMemberUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chatJoinRequest] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChatJoinRequest(block: suspend ActivityCtx<ChatJoinRequestUpdate>.() -> Unit) {
  onUpdate(UpdateType.CHAT_JOIN_REQUEST) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<ChatJoinRequestUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.chatBoost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onChatBoost(block: suspend ActivityCtx<ChatBoostUpdate>.() -> Unit) {
  onUpdate(UpdateType.CHAT_BOOST) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<ChatBoostUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.removedChatBoost] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onRemovedChatBoost(block: suspend ActivityCtx<RemovedChatBoostUpdate>.() -> Unit) {
  onUpdate(UpdateType.REMOVED_CHAT_BOOST) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<RemovedChatBoostUpdate>).block()
  }
}

/**
 * Action that is performed on the presence of [eu.vendeli.tgbot.types.common.Update.purchasedPaidMedia] in the [eu.vendeli.tgbot.types.common.Update].
 */
public fun FunctionalHandlingDsl.onPurchasedPaidMedia(block: suspend ActivityCtx<PurchasedPaidMediaUpdate>.() -> Unit) {
  onUpdate(UpdateType.PURCHASED_PAID_MEDIA) {
      @Suppress("UNCHECKED_CAST")
      (this as ActivityCtx<PurchasedPaidMediaUpdate>).block()
  }
}
