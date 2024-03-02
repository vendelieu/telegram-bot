package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.ChannelPostUpdate
import eu.vendeli.tgbot.types.internal.ChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ChatJoinRequestUpdate
import eu.vendeli.tgbot.types.internal.ChatMemberUpdate
import eu.vendeli.tgbot.types.internal.ChosenInlineResultUpdate
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
import eu.vendeli.tgbot.types.internal.RemovedChatBoostUpdate
import eu.vendeli.tgbot.types.internal.ShippingQueryUpdate

@Suppress("CyclomaticComplexMethod")
fun Update.processUpdate() = when {
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
    else -> throw IllegalArgumentException("Unknown type of update.")
}
