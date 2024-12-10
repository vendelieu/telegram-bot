package eu.vendeli.ktgram.extutils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.TgException

suspend inline fun <R> Action<R>.sendAnd(to: Long, bot: TelegramBot, block: suspend R.() -> Unit) {
    val result = sendReturning(to, bot).getOrNull() ?: throw TgException("Failed to process response")
    block(result)
}
