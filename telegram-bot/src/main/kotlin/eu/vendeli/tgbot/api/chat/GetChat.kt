@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAction : Action<Chat>, ActionState() {
    override val method: TgMethod = TgMethod("getChat")
    override val returnType = getReturnType()
}

fun getChat() = GetChatAction()
