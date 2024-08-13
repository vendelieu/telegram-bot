package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.interfaces.helper.Logger
import eu.vendeli.tgbot.types.internal.LogLvl
import org.slf4j.LoggerFactory

internal class LogbackLoggerWrapper : Logger {
    private val loggers = mutableMapOf<String, org.slf4j.Logger>()

    override suspend fun log(lvl: LogLvl, tag: String?, message: String, throwable: Throwable?) {
        val logger = getLogger(tag ?: DEFAULT_LOGGING_TAG)
        when (lvl) {
            LogLvl.DEBUG -> logger.debug(message)
            LogLvl.INFO -> logger.info(message)
            LogLvl.WARN -> logger.warn(message)
            LogLvl.ERROR -> logger.error(message, throwable)
            LogLvl.TRACE -> logger.trace(message)
            LogLvl.ALL -> logger.info(message)
            LogLvl.OFF -> {}
        }
    }

    private fun getLogger(tag: String): org.slf4j.Logger = loggers.getOrElse(tag) {
        LoggerFactory.getLogger(tag).also {
            loggers[tag] = it
        }
    }
}

private val logbackLogger by lazy { LogbackLoggerWrapper() }

internal actual val DEFAULT_LOGGER: Logger
    get() = logbackLogger
