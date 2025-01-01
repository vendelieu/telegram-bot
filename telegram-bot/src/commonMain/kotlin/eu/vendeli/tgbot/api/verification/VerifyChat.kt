@file:Suppress("MatchingDeclarationName", "ktlint:standard:filename")

package eu.vendeli.tgbot.api.verification

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class VerifyChatAction(
    chatId: Long,
    customDescription: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("verifyChat")
    override val method = "verifyChat"
    override val returnType = getReturnType()

    init {
        parameters["chat_id"] = chatId.toJsonElement()
        customDescription?.let { parameters["custom_description"] = it.toJsonElement() }
    }
}

/**
 * Verifies a chat on behalf of the organization which is represented by the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#verifychat)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun verifyChat(chatId: Long, customDescription: String? = null): VerifyChatAction =
    VerifyChatAction(chatId, customDescription)

@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun verifyChat(chatId: Long, customDescription: () -> String? = { null }): VerifyChatAction =
    verifyChat(chatId, customDescription())

@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun verifyChat(chat: Chat, customDescription: () -> String? = { null }): VerifyChatAction =
    verifyChat(chat.id, customDescription())
