@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.Chat
import eu.vendeli.tgbot.types.internal.TgMethod

class GetChatAction : Action<Chat> {
    override val method: TgMethod = TgMethod("getChat")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun getChat() = GetChatAction()
