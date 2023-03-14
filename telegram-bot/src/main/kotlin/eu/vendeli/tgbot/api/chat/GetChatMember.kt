@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMemberAction(userId: Long) : Action<ChatMember>, ActionState() {
    override val TgAction<ChatMember>.method: TgMethod
        get() = TgMethod("getChatMember")
    override val TgAction<ChatMember>.returnType: Class<ChatMember>
        get() = getReturnType()

    init {
        parameters["user_id"] = userId
    }
}

fun getChatMember(userId: Long) = GetChatMemberAction(userId)
