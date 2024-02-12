package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.LogLvl
import korlibs.logger.BaseConsole
import korlibs.logger.BaseConsole.Kind
import kotlinx.datetime.Clock

abstract class Logger(private val id: String) : io.ktor.client.plugins.logging.Logger {
    abstract fun setLevel(level: LogLvl)
    abstract fun info(message: () -> String): Unit?
    abstract fun warn(message: () -> String): Unit?
    abstract fun debug(message: () -> String): Unit?
    abstract fun trace(message: () -> String): Unit?
    abstract fun error(throwable: Throwable? = null, message: () -> String): Unit?
    override fun log(message: String) {
        trace { message }
    }
}

internal open class KorLogger(private val id: String) : Logger(id) {
    private val logger = BaseConsole()

    private var lvl = Kind.INFO

    override fun setLevel(level: LogLvl) {
        lvl = when (level) {
            LogLvl.OFF -> throw NotImplementedError()
            LogLvl.ERROR -> Kind.ERROR
            LogLvl.WARN -> Kind.WARN
            LogLvl.INFO -> Kind.INFO
            LogLvl.DEBUG -> Kind.DEBUG
            LogLvl.TRACE -> Kind.TRACE
            LogLvl.ALL -> Kind.LOG
        }
    }

    override fun info(message: () -> String) = log(Kind.INFO) { message() }
    override fun warn(message: () -> String) = log(Kind.WARN) { message() }
    override fun debug(message: () -> String) = log(Kind.DEBUG) { message() }
    override fun trace(message: () -> String) = log(Kind.TRACE) { message() }
    override fun error(throwable: Throwable?, message: () -> String) = log(Kind.ERROR, throwable) { message() }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun String.logEntry(
        kind: Kind,
    ) = "${Clock.System.now()} [$id] $kind $this"
    private inline fun log(level: Kind, throwable: Throwable? = null, message: () -> String): Unit? =
        if (level.ordinal <= lvl.ordinal)
            throwable?.let { logger.log(level, message().logEntry(level), it) }
                ?: logger.log(level, message().logEntry(level))
        else null
}

internal open class Logging(id: String = "TelegramBot") {
    val logger: Logger = KorLogger(id)
}
