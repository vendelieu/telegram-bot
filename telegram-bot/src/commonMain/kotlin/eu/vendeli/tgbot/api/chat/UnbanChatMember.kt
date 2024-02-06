@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class UnbanChatMemberAction(
    userId: Long,
    onlyIfBanned: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("unbanChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        if (onlyIfBanned != null) parameters["only_if_banned"] = onlyIfBanned.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatMember(userId: Long, onlyIfBanned: Boolean? = null) = UnbanChatMemberAction(userId, onlyIfBanned)

@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatMember(user: User, onlyIfBanned: Boolean? = null) = unbanChatMember(user.id, onlyIfBanned)
