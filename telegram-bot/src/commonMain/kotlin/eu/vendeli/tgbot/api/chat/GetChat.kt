@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatFullInfo
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetChatAction : Action<ChatFullInfo>() {
    @TgAPI.Name("getChat")
    override val method = "getChat"
    override val returnType = getReturnType()
}

/**
 * Use this method to get up-to-date information about the chat. Returns a ChatFullInfo object on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchat)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @returns [ChatFullInfo]
 */
@TgAPI
inline fun getChat() = GetChatAction()
