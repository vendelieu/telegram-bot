package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.ChatPermissions
import com.github.vendelieu.tgbot.types.internal.TgMethod

class RestrictChatMemberAction(
    userId: Long,
    permissions: ChatPermissions,
    untilDate: Int? = null,
) : Action<Boolean> {
    override val method: TgMethod = TgMethod("restrictChatMember")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        parameters["permissions"] = permissions
        if (untilDate != null) parameters["until_date"] = untilDate
    }
}

fun restrictChatMember(userId: Long, untilDate: Int? = null, chatPermissions: ChatPermissions.() -> Unit) =
    RestrictChatMemberAction(userId, ChatPermissions().apply(chatPermissions), untilDate)

fun restrictChatMember(userId: Long, chatPermissions: ChatPermissions, untilDate: Int? = null) =
    RestrictChatMemberAction(userId, chatPermissions, untilDate)
