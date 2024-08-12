@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.datetime.Instant

@TgAPI
class BanChatMemberAction(
    userId: Long,
    untilDate: Instant? = null,
    revokeMessages: Boolean? = null,
) : Action<Boolean>() {
    override val method = "banChatMember"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        if (untilDate != null) parameters["until_date"] = untilDate.epochSeconds.toJsonElement()
        if (revokeMessages != null) parameters["revoke_messages"] = revokeMessages.toJsonElement()
    }
}

/**
 * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the user will not be able to return to the chat on their own using invite links, etc., unless unbanned first. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#banchatmember)
 * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @param untilDate Date when the user will be unbanned; Unix time. If user is banned for more than 366 days or less than 30 seconds from the current time they are considered to be banned forever. Applied for supergroups and channels only.
 * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed. If False, the user will be able to see messages in the group that were sent before the user was removed. Always True for supergroups and channels.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun banChatMember(userId: Long, untilDate: Instant? = null, revokeMessages: Boolean? = null) =
    BanChatMemberAction(userId, untilDate, revokeMessages)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun banChatMember(user: User, untilDate: Instant? = null, revokeMessages: Boolean? = null) =
    banChatMember(user.id, untilDate, revokeMessages)
