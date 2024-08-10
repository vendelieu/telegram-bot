package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.types.internal.LogLvl

fun interface Logger {
    suspend fun log(lvl: LogLvl, tag: String?, message: String, throwable: Throwable?)
}
