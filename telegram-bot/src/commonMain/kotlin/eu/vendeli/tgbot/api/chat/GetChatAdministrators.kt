@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetChatAdministratorsAction(
    returnBots: Boolean? = null,
) : Action<List<ChatMember>>() {
    @TgAPI.Name("getChatAdministrators")
    override val method = "getChatAdministrators"
    override val returnType = getReturnType()

    init {
        if (returnBots != null) parameters["return_bots"] = returnBots.toJsonElement()
    }
}

/**
 * Use this method to get a list of administrators in a chat. Returns an Array of ChatMember objects.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatadministrators)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel in the format @username
 * @param returnBots Pass True to additionally receive all bots that are administrators of the chat. By default, bots other than the current bot are omitted.
 * @returns [Array of ChatMember]
 */
@TgAPI
inline fun getChatAdministrators(returnBots: Boolean? = null) = GetChatAdministratorsAction(returnBots)
