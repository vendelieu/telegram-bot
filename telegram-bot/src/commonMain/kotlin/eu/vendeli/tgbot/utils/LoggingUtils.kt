package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.interfaces.helper.Logger
import eu.vendeli.tgbot.types.internal.LogLvl
import eu.vendeli.tgbot.types.internal.configuration.LoggingConfiguration

class LoggingWrapper(
    private val cfg: LoggingConfiguration,
    private val tag: String,
) {
    suspend fun info(message: () -> String) = log(LogLvl.INFO, message, null)
    suspend fun warn(message: () -> String) = log(LogLvl.WARN, message, null)
    suspend fun debug(message: () -> String) = log(LogLvl.DEBUG, message, null)
    suspend fun trace(message: () -> String) = log(LogLvl.TRACE, message, null)
    suspend fun error(throwable: Throwable? = null, message: () -> String) =
        log(LogLvl.ERROR, message, throwable)

    private suspend inline fun log(logLvl: LogLvl, message: () -> String, throwable: Throwable?) =
        if (cfg.botLogLevel.int < logLvl.int) cfg.logger.log(logLvl, tag, message(), throwable) else Unit
}

internal expect val DEFAULT_LOGGER: Logger
