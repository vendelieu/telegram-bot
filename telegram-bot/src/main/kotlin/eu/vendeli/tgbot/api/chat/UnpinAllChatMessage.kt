@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnpinAllChatMessageAction : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("unpinAllChatMessages")
    override val returnType = getReturnType()
}

fun unpinAllChatMessage() = UnpinAllChatMessageAction()
