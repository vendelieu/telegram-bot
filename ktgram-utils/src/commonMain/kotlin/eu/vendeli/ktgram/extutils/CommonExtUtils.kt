package eu.vendeli.ktgram.extutils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.types.component.getOrNull
import eu.vendeli.tgbot.utils.common.TgException

private val DEFAULT_FAILURE_ACTION: suspend () -> Nothing = { throw TgException("Failed to process response") }

/**
 * Sends action to the specified [to] and executes [block] on result of the action.
 * If the action fails, [onFailure] is called.
 *
 * @param to identifier of the target.
 * @param bot Telegram bot to send the action.
 * @param onFailure Function to call if the action fails, by default throws [TgException].
 * @param block Function to execute on result of the action.
 */
private suspend fun <R> Action<R>.sendAnd(
    to: Identifier,
    bot: TelegramBot,
    onFailure: suspend () -> Nothing = DEFAULT_FAILURE_ACTION,
    block: suspend R.() -> Unit,
) {
    val result = when (to) {
        is Identifier.String -> {
            sendReturning(to.get, bot).getOrNull()
        }

        is Identifier.Long -> {
            sendReturning(to.get, bot).getOrNull()
        }
    } ?: onFailure()
    block(result)
}

suspend fun <R> Action<R>.sendAnd(
    to: Long,
    bot: TelegramBot,
    onFailure: suspend () -> Nothing = DEFAULT_FAILURE_ACTION,
    block: suspend R.() -> Unit,
) = sendAnd(Identifier.from(to), bot, onFailure, block)

suspend fun <R> Action<R>.sendAnd(
    to: String,
    bot: TelegramBot,
    onFailure: suspend () -> Nothing = DEFAULT_FAILURE_ACTION,
    block: suspend R.() -> Unit,
) = sendAnd(Identifier.from(to), bot, onFailure, block)

suspend fun <R> Action<R>.sendAnd(
    to: Chat,
    bot: TelegramBot,
    onFailure: suspend () -> Nothing = DEFAULT_FAILURE_ACTION,
    block: suspend R.() -> Unit,
) = sendAnd(Identifier.from(to), bot, onFailure, block)

suspend fun <R> Action<R>.sendAnd(
    to: User,
    bot: TelegramBot,
    onFailure: suspend () -> Nothing = DEFAULT_FAILURE_ACTION,
    block: suspend R.() -> Unit,
) = sendAnd(Identifier.from(to), bot, onFailure, block)
