package eu.vendeli.tgbot.types.internal.chain

import eu.vendeli.tgbot.types.internal.ProcessedUpdate

fun interface KeySelector<T> {
    fun select(update: ProcessedUpdate): T?
}
