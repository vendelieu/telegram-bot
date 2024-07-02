@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class BanChatSenderChatAction(
    senderChatId: Long,
) : Action<Boolean>() {
    override val method = TgMethod("banChatSenderChat")
    override val returnType = getReturnType()

    init {
        parameters["sender_chat_id"] = senderChatId.toJsonElement()
    }
}

/**
 * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is unbanned, the owner of the banned chat won't be able to send messages on behalf of any of their channels. The bot must be an administrator in the supergroup or channel for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#banchatsenderchat)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param senderChatId Unique identifier of the target sender chat
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: Long) = BanChatSenderChatAction(senderChatId)

@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: Chat) = banChatSenderChat(senderChatId.id)

@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: User) = banChatSenderChat(senderChatId.id)
