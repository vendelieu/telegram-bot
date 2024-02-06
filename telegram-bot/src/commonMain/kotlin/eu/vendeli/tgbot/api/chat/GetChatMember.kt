@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class GetChatMemberAction(userId: Long) : Action<ChatMember>() {
    override val method = TgMethod("getChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getChatMember(userId: Long) = GetChatMemberAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun getChatMember(user: User) = getChatMember(user.id)
