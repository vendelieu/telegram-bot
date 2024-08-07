@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class UnbanChatSenderChatAction(
    senderChatId: Long,
) : Action<Boolean>() {
    override val method = "unbanChatSenderChat"
    override val returnType = getReturnType()

    init {
        parameters["sender_chat_id"] = senderChatId.toJsonElement()
    }
}

/**
 * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an administrator for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unbanchatsenderchat)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param senderChatId Unique identifier of the target sender chat
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatSenderChat(senderChatId: Long) = UnbanChatSenderChatAction(senderChatId)

@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatSenderChat(senderChatId: Chat) = unbanChatSenderChat(senderChatId.id)

@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatSenderChat(senderChatId: User) = unbanChatSenderChat(senderChatId.id)
