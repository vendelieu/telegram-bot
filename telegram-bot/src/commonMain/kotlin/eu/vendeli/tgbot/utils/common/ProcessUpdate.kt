package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.common.Update
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
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.PurchasedPaidMediaUpdate
import eu.vendeli.tgbot.types.component.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.component.ShippingQueryUpdate

@KtGramInternal
@Suppress("CyclomaticComplexMethod")
fun Update.processUpdate(): ProcessedUpdate = when {
    message != null -> MessageUpdate(updateId, this, message)
    editedMessage != null -> EditedMessageUpdate(updateId, this, editedMessage)
    channelPost != null -> ChannelPostUpdate(updateId, this, channelPost)
    editedChannelPost != null -> EditedChannelPostUpdate(updateId, this, editedChannelPost)
    messageReaction != null -> MessageReactionUpdate(updateId, this, messageReaction)
    messageReactionCount != null -> MessageReactionCountUpdate(updateId, this, messageReactionCount)
    inlineQuery != null -> InlineQueryUpdate(updateId, this, inlineQuery)
    chosenInlineResult != null -> ChosenInlineResultUpdate(updateId, this, chosenInlineResult)
    callbackQuery != null -> CallbackQueryUpdate(updateId, this, callbackQuery)
    shippingQuery != null -> ShippingQueryUpdate(updateId, this, shippingQuery)
    preCheckoutQuery != null -> PreCheckoutQueryUpdate(updateId, this, preCheckoutQuery)
    poll != null -> PollUpdate(updateId, this, poll)
    pollAnswer != null -> PollAnswerUpdate(updateId, this, pollAnswer)
    myChatMember != null -> MyChatMemberUpdate(updateId, this, myChatMember)
    chatMember != null -> ChatMemberUpdate(updateId, this, chatMember)
    chatJoinRequest != null -> ChatJoinRequestUpdate(updateId, this, chatJoinRequest)
    chatBoost != null -> ChatBoostUpdate(updateId, this, chatBoost)
    removedChatBoost != null -> RemovedChatBoostUpdate(updateId, this, removedChatBoost)
    businessConnection != null -> BusinessConnectionUpdate(updateId, this, businessConnection)
    businessMessage != null -> BusinessMessageUpdate(updateId, this, businessMessage)
    editedBusinessMessage != null -> EditedBusinessMessageUpdate(updateId, this, editedBusinessMessage)
    deletedBusinessMessages != null -> DeletedBusinessMessagesUpdate(updateId, this, deletedBusinessMessages)
    purchasedPaidMedia != null -> PurchasedPaidMediaUpdate(updateId, this, purchasedPaidMedia)
    else -> throw TgException("Unknown type of update.")
}
