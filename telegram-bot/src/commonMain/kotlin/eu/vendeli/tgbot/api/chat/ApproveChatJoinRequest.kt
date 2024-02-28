@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class ApproveChatJoinRequestAction(userId: Long) : Action<Boolean>() {
    override val method = TgMethod("approveChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#approvechatjoinrequest
*/
@Suppress("NOTHING_TO_INLINE")
inline fun approveChatJoinRequest(userId: Long) = ApproveChatJoinRequestAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun approveChatJoinRequest(user: User) = approveChatJoinRequest(user.id)
