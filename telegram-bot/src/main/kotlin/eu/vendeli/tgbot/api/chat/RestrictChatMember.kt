@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.ChatPermissions
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class RestrictChatMemberAction(
    userId: Long,
    permissions: ChatPermissions,
    untilDate: Int? = null,
    useIndependentChatPermissions: Boolean? = null,
) : Action<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("restrictChatMember")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        parameters["user_id"] = userId
        parameters["permissions"] = permissions
        if (untilDate != null) parameters["until_date"] = untilDate
        if (useIndependentChatPermissions != null)
            parameters["use_independent_chat_permissions"] = useIndependentChatPermissions
    }
}

fun restrictChatMember(
    userId: Long,
    untilDate: Int? = null,
    useIndependentChatPermissions: Boolean? = null,
    chatPermissions: ChatPermissions.() -> Unit,
) = RestrictChatMemberAction(userId, ChatPermissions().apply(chatPermissions), untilDate, useIndependentChatPermissions)

fun restrictChatMember(
    userId: Long,
    chatPermissions: ChatPermissions,
    untilDate: Int? = null,
    useIndependentChatPermissions: Boolean? = null,
) = RestrictChatMemberAction(userId, chatPermissions, untilDate, useIndependentChatPermissions)
