@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class UnbanChatMemberAction(
    userId: Long,
    onlyIfBanned: Boolean? = null,
) : Action<Boolean>() {
    @TgAPI.Name("unbanChatMember")
    override val method = "unbanChatMember"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        if (onlyIfBanned != null) parameters["only_if_banned"] = onlyIfBanned.toJsonElement()
    }
}

/**
 * Use this method to unban a previously banned user in a supergroup or channel. The user will not return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be able to join it. So if the user is a member of the chat they will also be removed from the chat. If you don't want this, use the parameter only_if_banned. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unbanchatmember)
 * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @param onlyIfBanned Do nothing if the user is not banned
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun unbanChatMember(userId: Long, onlyIfBanned: Boolean? = null) = UnbanChatMemberAction(userId, onlyIfBanned)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun unbanChatMember(user: User, onlyIfBanned: Boolean? = null) = unbanChatMember(user.id, onlyIfBanned)
