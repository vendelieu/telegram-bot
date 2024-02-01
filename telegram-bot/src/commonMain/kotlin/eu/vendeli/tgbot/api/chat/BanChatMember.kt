@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.datetime.Instant

class BanChatMemberAction(
    userId: Long,
    untilDate: Instant? = null,
    revokeMessages: Boolean? = null,
) : Action<Boolean>() {
    override val method = TgMethod("banChatMember")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        if (untilDate != null) parameters["until_date"] = untilDate.epochSeconds.toJsonElement()
        if (revokeMessages != null) parameters["revoke_messages"] = revokeMessages.toJsonElement()
    }
}

inline fun banChatMember(userId: Long, untilDate: Instant? = null, revokeMessages: Boolean? = null) =
    BanChatMemberAction(userId, untilDate, revokeMessages)

inline fun banChatMember(user: User, untilDate: Instant? = null, revokeMessages: Boolean? = null) =
    banChatMember(user.id, untilDate, revokeMessages)
