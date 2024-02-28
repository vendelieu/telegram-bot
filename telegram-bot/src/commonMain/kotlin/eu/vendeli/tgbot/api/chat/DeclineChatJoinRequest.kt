@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeclineChatJoinRequestAction(userId: Long) : Action<Boolean>() {
    override val method = TgMethod("declineChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#declinechatjoinrequest
*/
@Suppress("NOTHING_TO_INLINE")
inline fun declineChatJoinRequest(userId: Long) = DeclineChatJoinRequestAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun declineChatJoinRequest(user: User) = declineChatJoinRequest(user.id)
