@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMemberCountAction : Action<Int>, ActionState() {
    override val TgAction<Int>.method: TgMethod
        get() = TgMethod("getChatMemberCount")
    override val TgAction<Int>.returnType: Class<Int>
        get() = getReturnType()
}

fun getChatMemberCount() = GetChatMemberCountAction()
