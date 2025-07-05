@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetChatAdministratorsAction : Action<List<ChatMember>>() {
    @TgAPI.Name("getChatAdministrators")
    override val method = "getChatAdministrators"
    override val returnType = getReturnType()
}

/**
 * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of ChatMember objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatadministrators)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @returns [Array of ChatMember]
 */
@TgAPI
inline fun getChatAdministrators() = GetChatAdministratorsAction()
