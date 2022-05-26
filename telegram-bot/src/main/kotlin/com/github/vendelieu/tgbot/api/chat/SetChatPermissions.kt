package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.ChatPermissions
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetChatPermissionsAction(permissions: ChatPermissions) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatPermissions")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["permissions"] = permissions
    }
}

fun setChatPermissions(permissions: ChatPermissions) = SetChatPermissionsAction(permissions)
fun setChatPermissions(permissions: ChatPermissions.() -> Unit) =
    SetChatPermissionsAction(ChatPermissions().apply(permissions))
