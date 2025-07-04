@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class BanChatSenderChatAction(
    senderChatId: Long,
) : Action<Boolean>() {
    @TgAPI.Name("banChatSenderChat")
    override val method = "banChatSenderChat"
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
@TgAPI
inline fun banChatSenderChat(senderChatId: Long) = BanChatSenderChatAction(senderChatId)

@TgAPI
inline fun banChatSenderChat(senderChatId: Chat) = banChatSenderChat(senderChatId.id)

@TgAPI
inline fun banChatSenderChat(senderChatId: User) = banChatSenderChat(senderChatId.id)
