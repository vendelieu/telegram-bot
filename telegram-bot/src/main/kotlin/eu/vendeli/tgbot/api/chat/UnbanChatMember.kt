package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class UnbanChatMemberAction(
    userId: Long,
    onlyIfBanned: Boolean? = null,
) : Action<Boolean> {
    override val method: TgMethod = TgMethod("unbanChatMember")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        if (onlyIfBanned != null) parameters["only_if_banned"] = onlyIfBanned
    }
}

fun unbanChatMember(userId: Long, onlyIfBanned: Boolean?) = UnbanChatMemberAction(userId, onlyIfBanned)
