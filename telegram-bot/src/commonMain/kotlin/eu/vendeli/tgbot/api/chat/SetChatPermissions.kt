@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatPermissionsAction(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("setChatPermissions")
    override val returnType = getReturnType()

    init {
        parameters["permissions"] = permissions.encodeWith(ChatPermissions.serializer())
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions.toJsonElement()
    }
}

/**
 * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a supergroup for this to work and must have the can_restrict_members administrator rights. Returns True on success.
 * @param chatId Required 
 * @param permissions Required 
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchatpermissions
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatPermissions(
    permissions: ChatPermissions,
    useIndependentChatPermissions: Boolean? = null,
) = SetChatPermissionsAction(permissions, useIndependentChatPermissions)
inline fun setChatPermissions(
    useIndependentChatPermissions: Boolean? = null,
    permissions: ChatPermissions.() -> Unit,
) = setChatPermissions(ChatPermissions().apply(permissions), useIndependentChatPermissions)
