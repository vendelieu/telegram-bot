@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMemberCountAction : Action<Int>() {
    override val method = TgMethod("getChatMemberCount")
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
inline fun getChatMemberCount() = GetChatMemberCountAction()
