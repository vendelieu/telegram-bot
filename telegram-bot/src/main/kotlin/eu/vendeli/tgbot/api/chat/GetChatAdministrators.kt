@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAdministratorsAction : Action<List<ChatMember>>() {
    override val method = TgMethod("getChatAdministrators")
    override val returnType = getReturnType()
}

@Suppress("NOTHING_TO_INLINE")
inline fun getChatAdministrators() = GetChatAdministratorsAction()
