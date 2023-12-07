@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class BanChatMemberAction(
    userId: Long,
    untilDate: Long? = null,
    revokeMessages: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("banChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
        if (untilDate != null) parameters["until_date"] = untilDate
        if (revokeMessages != null) parameters["revoke_messages"] = revokeMessages
    }
}

fun banChatMember(userId: Long, untilDate: Long? = null, revokeMessages: Boolean? = null) =
    BanChatMemberAction(userId, untilDate, revokeMessages)
fun banChatMember(user: User, untilDate: Long? = null, revokeMessages: Boolean? = null) =
    BanChatMemberAction(user.id, untilDate, revokeMessages)
