@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class LeaveChatAction : Action<Boolean>() {
    @TgAPI.Name("leaveChat")
    override val method = "leaveChat"
    override val returnType = getReturnType()
}

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#leavechat)
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername). Channel direct messages chats aren't supported; leave the corresponding channel instead.
 * @returns [Boolean]
 */
@TgAPI
inline fun leaveChat() = LeaveChatAction()
