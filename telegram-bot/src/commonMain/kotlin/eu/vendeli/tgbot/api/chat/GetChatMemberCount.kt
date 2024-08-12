@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.getReturnType

@TgAPI
class GetChatMemberCountAction : Action<Int>() {
    @TgAPI.Name("getChatMemberCount")
    override val method = "getChatMemberCount"
    override val returnType = getReturnType()
}

/**
 * Use this method to get the number of members in a chat. Returns Int on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatmembercount)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @returns [Integer]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun getChatMemberCount() = GetChatMemberCountAction()
