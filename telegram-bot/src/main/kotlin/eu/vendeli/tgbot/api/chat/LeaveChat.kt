@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class LeaveChatAction : Action<Boolean>() {
    override val method = TgMethod("leaveChat")
    override val returnType = getReturnType()
}

fun leaveChat() = LeaveChatAction()
