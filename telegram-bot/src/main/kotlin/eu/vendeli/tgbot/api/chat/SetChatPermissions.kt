@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatPermissionsAction(permissions: ChatPermissions) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatPermissions")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["permissions"] = permissions
    }
}

fun setChatPermissions(permissions: ChatPermissions) = SetChatPermissionsAction(permissions)
fun setChatPermissions(permissions: ChatPermissions.() -> Unit) =
    SetChatPermissionsAction(ChatPermissions().apply(permissions))
