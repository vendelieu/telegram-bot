@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAction : Action<Chat>, ActionState() {
    override val TgAction<Chat>.method: TgMethod
        get() = TgMethod("getChat")
    override val TgAction<Chat>.returnType: Class<Chat>
        get() = getReturnType()
}

fun getChat() = GetChatAction()
