package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.types.component.LogLvl
import io.ktor.util.logging.Logger

internal expect inline fun getLogger(lvl: LogLvl, tag: String): Logger

internal inline fun Logger.info(message: () -> String) = info(message())

internal inline fun Logger.warn(message: () -> String) = warn(message())

internal inline fun Logger.debug(message: () -> String) = debug(message())

internal inline fun Logger.error(throwable: Throwable? = null, message: () -> String) =
    throwable?.let { error(message(), it) } ?: error(message())
