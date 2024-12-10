package eu.vendeli.ktgram.extutils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.TgException

private suspend inline fun <R> Action<R>.sendAnd(to: Identifier, bot: TelegramBot, block: suspend R.() -> Unit) {
    val result = when (to) {
        is Identifier.String -> {
            sendReturning(to.get, bot).getOrNull()
        }

        is Identifier.Long -> {
            sendReturning(to.get, bot).getOrNull()
        }
    } ?: throw TgException("Failed to process response")
    block(result)
}

suspend fun <R> Action<R>.sendAnd(to: Long, bot: TelegramBot, block: suspend R.() -> Unit) =
    sendAnd(Identifier.from(to), bot, block)

suspend fun <R> Action<R>.sendAnd(to: String, bot: TelegramBot, block: suspend R.() -> Unit) =
    sendAnd(Identifier.from(to), bot, block)

suspend fun <R> Action<R>.sendAnd(to: Chat, bot: TelegramBot, block: suspend R.() -> Unit) =
    sendAnd(Identifier.from(to.id), bot, block)

suspend fun <R> Action<R>.sendAnd(to: User, bot: TelegramBot, block: suspend R.() -> Unit) =
    sendAnd(Identifier.from(to), bot, block)
