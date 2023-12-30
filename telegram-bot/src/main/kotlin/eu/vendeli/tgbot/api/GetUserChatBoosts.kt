@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.UserChatBoosts
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetUserChatBoostsAction(
    val userId: Long,
) : Action<UserChatBoosts>() {
    override val method = TgMethod("getUserChatBoosts")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun getUserChatBoosts(userId: Long) = GetUserChatBoostsAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun getUserChatBoosts(user: User) = getUserChatBoosts(user.id)

