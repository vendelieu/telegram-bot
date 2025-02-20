package eu.vendeli.tgbot.types.chain

import eu.vendeli.tgbot.types.component.ProcessedUpdate

fun interface KeySelector<T> {
    fun select(update: ProcessedUpdate): T?
}
