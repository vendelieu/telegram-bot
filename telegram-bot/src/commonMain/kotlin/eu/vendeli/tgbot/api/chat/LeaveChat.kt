@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class LeaveChatAction : Action<Boolean>() {
    override val method = TgMethod("leaveChat")
    override val returnType = getReturnType()
}

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#leavechat)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun leaveChat() = LeaveChatAction()
