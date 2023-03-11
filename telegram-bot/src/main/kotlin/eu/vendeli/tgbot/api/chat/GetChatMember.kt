@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMemberAction(userId: Long) : Action<ChatMember>, ActionState() {
    override val method: TgMethod = TgMethod("getChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
    }
}

fun getChatMember(userId: Long) = GetChatMemberAction(userId)
