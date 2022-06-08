package com.github.vendelieu.tgbot.interfaces

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.types.internal.ProcessedUpdate

/**
 * Interface for implementing the dynamic acquisition of a magic object
 *
 * @param T
 */
interface MagicObject<T> {
    fun get(update: ProcessedUpdate, bot: TelegramBot): T?
}
