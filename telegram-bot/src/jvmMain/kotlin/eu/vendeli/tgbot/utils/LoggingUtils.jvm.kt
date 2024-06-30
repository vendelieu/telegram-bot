package eu.vendeli.tgbot.utils

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.types.internal.LogLvl
import org.slf4j.LoggerFactory

internal open class LogbackLogger(id: String) : Logger(id) {
    private val logger = LoggerFactory.getLogger(id)

    private var lvl = Level.INFO

    override fun setLevel(level: LogLvl) {
        lvl = when (level) {
            LogLvl.OFF -> Level.OFF
            LogLvl.ERROR -> Level.ERROR
            LogLvl.WARN -> Level.WARN
            LogLvl.INFO -> Level.INFO
            LogLvl.DEBUG -> Level.DEBUG
            LogLvl.TRACE -> Level.TRACE
            LogLvl.ALL -> Level.TRACE
        }
        val id = if (id == "eu.vendeli.TelegramBot") "eu.vendeli" else id
        val root = LoggerFactory.getLogger(id) as ch.qos.logback.classic.Logger
        root.setLevel(lvl)
    }

    override fun info(message: () -> String) {
        logger.info(message())
    }

    override fun warn(message: () -> String) {
        logger.warn(message())
    }

    override fun debug(message: () -> String) {
        logger.debug(message())
    }

    override fun trace(message: () -> String) {
        logger.trace(message())
    }

    override fun error(throwable: Throwable?, message: () -> String) {
        logger.error(message(), throwable)
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual open class Logging actual constructor(tag: String) {
    actual val logger: Logger = LogbackLogger(tag)
}
