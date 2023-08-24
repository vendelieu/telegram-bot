@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnbanChatMemberAction(
    userId: Long,
    onlyIfBanned: Boolean? = null,
) : Action<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("unbanChatMember")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        parameters["user_id"] = userId
        if (onlyIfBanned != null) parameters["only_if_banned"] = onlyIfBanned
    }
}

fun unbanChatMember(userId: Long, onlyIfBanned: Boolean? = null) = UnbanChatMemberAction(userId, onlyIfBanned)
