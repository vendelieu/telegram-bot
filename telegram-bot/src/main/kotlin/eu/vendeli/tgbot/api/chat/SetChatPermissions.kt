@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatPermissionsAction(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatPermissions")
    override val returnType = getReturnType()

    init {
        parameters["permissions"] = permissions
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions
    }
}

fun setChatPermissions(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) = SetChatPermissionsAction(permissions, useIndependentChatPermissions)

fun setChatPermissions(
    useIndependentChatPermissions: Boolean? = null,
    permissions: ChatPermissions.() -> Unit,
) = SetChatPermissionsAction(ChatPermissions().apply(permissions), useIndependentChatPermissions)
