package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType

internal fun processUpdate(update: Update): ProcessedUpdate = when {
    update.message != null -> ProcessedUpdate(
        type = UpdateType.MESSAGE,
        text = update.message.text,
        user = update.message.from!!,
        fullUpdate = update
    )

    update.editedMessage != null -> ProcessedUpdate(
        type = UpdateType.EDIT_MESSAGE,
        text = update.editedMessage.text,
        user = update.editedMessage.from!!,
        fullUpdate = update
    )

    update.channelPost != null -> ProcessedUpdate(
        type = UpdateType.CHANNEL_POST,
        text = update.channelPost.text,
        user = update.channelPost.from ?: User.EMPTY,
        fullUpdate = update
    )

    update.editedChannelPost != null -> ProcessedUpdate(
        type = UpdateType.EDITED_CHANNEL_POST,
        text = update.editedChannelPost.text,
        user = update.editedChannelPost.from ?: User.EMPTY,
        fullUpdate = update
    )

    update.inlineQuery != null -> ProcessedUpdate(
        type = UpdateType.INLINE_QUERY,
        text = update.inlineQuery.query,
        user = update.inlineQuery.from,
        fullUpdate = update
    )

    update.chosenInlineResult != null -> ProcessedUpdate(
        type = UpdateType.CHOSEN_INLINE_RESULT,
        text = update.chosenInlineResult.query,
        user = update.chosenInlineResult.from,
        fullUpdate = update
    )

    update.callbackQuery != null -> ProcessedUpdate(
        type = UpdateType.CALLBACK_QUERY,
        text = update.callbackQuery.data,
        user = update.callbackQuery.from,
        fullUpdate = update
    )

    update.shippingQuery != null -> ProcessedUpdate(
        type = UpdateType.SHIPPING_QUERY,
        text = update.shippingQuery.invoicePayload,
        user = update.shippingQuery.from,
        fullUpdate = update
    )

    update.preCheckoutQuery != null -> ProcessedUpdate(
        type = UpdateType.PRE_CHECKOUT_QUERY,
        text = update.preCheckoutQuery.invoicePayload,
        user = update.preCheckoutQuery.from,
        fullUpdate = update
    )

    update.poll != null -> ProcessedUpdate(
        type = UpdateType.POLL,
        text = update.poll.question,
        user = User.EMPTY,
        fullUpdate = update
    )

    update.pollAnswer != null -> ProcessedUpdate(
        type = UpdateType.POLL_ANSWER,
        text = null,
        user = update.pollAnswer.user,
        fullUpdate = update
    )

    update.myChatMember != null -> ProcessedUpdate(
        type = UpdateType.MY_CHAT_MEMBER,
        text = null,
        user = update.myChatMember.from,
        fullUpdate = update
    )

    update.chatMember != null -> ProcessedUpdate(
        type = UpdateType.CHAT_MEMBER,
        text = null,
        user = update.chatMember.from,
        fullUpdate = update
    )

    update.chatJoinRequest != null -> ProcessedUpdate(
        type = UpdateType.CHAT_JOIN_REQUEST,
        text = null,
        user = update.chatJoinRequest.from,
        fullUpdate = update
    )

    else -> ProcessedUpdate(UpdateType.UNKNOWN, "", User.EMPTY, update)
}