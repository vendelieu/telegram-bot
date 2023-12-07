@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnbanChatMemberAction(
    userId: Long,
    onlyIfBanned: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("unbanChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
        if (onlyIfBanned != null) parameters["only_if_banned"] = onlyIfBanned
    }
}

fun unbanChatMember(userId: Long, onlyIfBanned: Boolean? = null) = UnbanChatMemberAction(userId, onlyIfBanned)
fun unbanChatMember(user: User, onlyIfBanned: Boolean? = null) = UnbanChatMemberAction(user.id, onlyIfBanned)
