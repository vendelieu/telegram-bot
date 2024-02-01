package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.LogLvl
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

abstract class Logger(private val id: String) {
    abstract fun setLevel(level: LogLvl)
    abstract fun info(message: () -> String): Unit?
    abstract fun debug(message: () -> String): Unit?
    abstract fun trace(message: () -> String): Unit?
    abstract fun error(throwable: Throwable? = null, message: () -> String): Unit?
}

internal open class NapierLogger(private val id: String) : Logger(id) {
    init {
        Napier.base(DebugAntilog(id))
    }

    private var lvl = LogLevel.INFO

    override fun setLevel(level: LogLvl) {
        lvl = when (level) {
            LogLvl.OFF -> LogLevel.ASSERT
            LogLvl.ERROR -> LogLevel.ERROR
            LogLvl.WARN -> LogLevel.WARNING
            LogLvl.INFO -> LogLevel.INFO
            LogLvl.DEBUG -> LogLevel.DEBUG
            LogLvl.TRACE -> LogLevel.VERBOSE
            LogLvl.ALL -> LogLevel.VERBOSE
        }
    }

    override fun info(message: () -> String) = log(LogLevel.INFO) { message() }
    override fun debug(message: () -> String) = log(LogLevel.DEBUG) { message() }
    override fun trace(message: () -> String) = log(LogLevel.VERBOSE) { message() }
    override fun error(throwable: Throwable?, message: () -> String) = log(LogLevel.ERROR, throwable) { message() }

    private inline fun log(level: LogLevel, throwable: Throwable? = null, message: () -> String): Unit? =
        if (level.ordinal <= lvl.ordinal) Napier.log(level, id, throwable, message()) else null
}

internal open class Logging(id: String = "TelegramBot") {
    val logger: Logger = NapierLogger(id)
}
