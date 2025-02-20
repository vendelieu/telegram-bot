@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class ApproveChatJoinRequestAction(
    userId: Long,
) : Action<Boolean>() {
    @TgAPI.Name("approveChatJoinRequest")
    override val method = "approveChatJoinRequest"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#approvechatjoinrequest)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun approveChatJoinRequest(userId: Long) = ApproveChatJoinRequestAction(userId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun approveChatJoinRequest(user: User) = approveChatJoinRequest(user.id)
