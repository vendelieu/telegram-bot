@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatPermissionsAction(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatPermissions")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["permissions"] = permissions
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions
    }
}

fun setChatPermissions(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) = SetChatPermissionsAction(permissions)

fun setChatPermissions(
    useIndependentChatPermissions: Boolean? = null,
    permissions: ChatPermissions.() -> Unit,
) = SetChatPermissionsAction(ChatPermissions().apply(permissions))
