@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatPermissionsAction(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("setChatPermissions")
    override val returnType = getReturnType()

    init {
        parameters["permissions"] = permissions
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setChatPermissions(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) = SetChatPermissionsAction(permissions, useIndependentChatPermissions)
inline fun setChatPermissions(
    useIndependentChatPermissions: Boolean? = null,
    permissions: ChatPermissions.() -> Unit,
) = setChatPermissions(ChatPermissions().apply(permissions), useIndependentChatPermissions)
