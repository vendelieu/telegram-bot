@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class BanChatMemberAction(
    userId: Long,
    untilDate: Int? = null,
    revokeMessages: Boolean? = null,
) : Action<Boolean> {
    override val method: TgMethod = TgMethod("banChatMember")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        if (untilDate != null) parameters["until_date"] = untilDate
        if (revokeMessages != null) parameters["revoke_messages"] = revokeMessages
    }
}

fun banChatMember(userId: Long, untilDate: Int?, revokeMessages: Boolean?) =
    BanChatMemberAction(userId, untilDate, revokeMessages)
