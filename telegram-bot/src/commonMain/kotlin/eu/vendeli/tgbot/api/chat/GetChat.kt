@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAction : Action<Chat>() {
    override val method = TgMethod("getChat")
    override val returnType = getReturnType()
}

inline fun getChat() = GetChatAction()
