@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMemberCountAction : Action<Int>, ActionState() {
    override val method: TgMethod = TgMethod("getChatMemberCount")
    override val returnType = getReturnType()
}

fun getChatMemberCount() = GetChatMemberCountAction()
