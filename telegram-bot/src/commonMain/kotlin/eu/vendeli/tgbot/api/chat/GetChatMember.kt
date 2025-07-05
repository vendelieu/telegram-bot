@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class GetChatMemberAction(
    userId: Long,
) : Action<ChatMember>() {
    @TgAPI.Name("getChatMember")
    override val method = "getChatMember"
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to get information about a member of a chat. The method is only guaranteed to work for other users if the bot is an administrator in the chat. Returns a ChatMember object on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatmember)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @returns [ChatMember]
 */
@TgAPI
inline fun getChatMember(userId: Long) = GetChatMemberAction(userId)

@TgAPI
inline fun getChatMember(user: User) = getChatMember(user.id)
