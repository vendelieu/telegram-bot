@file:Suppress("MatchingDeclarationName", "ktlint:standard:filename")

package eu.vendeli.tgbot.api.verification

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class RemoveChatVerificationAction(
    chatId: Long,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("removeChatVerification")
    override val method = "removeChatVerification"
    override val returnType = getReturnType()

    init {
        parameters["chat_id"] = chatId.toJsonElement()
    }
}

/**
 * Removes verification from a chat that is currently verified on behalf of the organization represented by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#removechatverification)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @returns [Boolean]
 */
@TgAPI
inline fun removeChatVerification(chatId: Long): RemoveChatVerificationAction =
    RemoveChatVerificationAction(chatId)

@TgAPI
inline fun removeChatVerification(chat: Chat): RemoveChatVerificationAction =
    removeChatVerification(chat.id)
