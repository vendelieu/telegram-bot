@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.boost.UserChatBoosts
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetUserChatBoostsAction(
    val userId: Long,
) : Action<UserChatBoosts>() {
    @TgAPI.Name("getUserChatBoosts")
    override val method = "getUserChatBoosts"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to get the list of boosts added to a chat by a user. Requires administrator rights in the chat. Returns a UserChatBoosts object.
 *
 * [Api reference](https://core.telegram.org/bots/api#getuserchatboosts)
 * @param chatId Unique identifier for the chat or username of the channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @returns [UserChatBoosts]
 */
@TgAPI
inline fun getUserChatBoosts(userId: Long) = GetUserChatBoostsAction(userId)

@TgAPI
inline fun getUserChatBoosts(user: User) = getUserChatBoosts(user.id)
