@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class RestrictChatMemberAction(
    userId: Long,
    permissions: ChatPermissions,
    untilDate: Long? = null,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("restrictChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
        parameters["permissions"] = permissions
        if (untilDate != null) parameters["until_date"] = untilDate
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions
    }
}

inline fun restrictChatMember(
    userId: Long,
    untilDate: Long? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatPermissions: ChatPermissions.() -> Unit,
) = RestrictChatMemberAction(userId, ChatPermissions().apply(chatPermissions), untilDate, useIndependentChatPermissions)

@Suppress("NOTHING_TO_INLINE")
inline fun restrictChatMember(
    userId: Long,
    chatPermissions: ChatPermissions,
    untilDate: Long? = null,
    useIndependentChatPermissions: Boolean? = null,
) = RestrictChatMemberAction(userId, chatPermissions, untilDate, useIndependentChatPermissions)
