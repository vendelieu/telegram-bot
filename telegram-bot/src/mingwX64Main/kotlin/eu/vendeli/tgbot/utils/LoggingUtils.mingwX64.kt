package eu.vendeli.tgbot.utils

import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.mutableLoggerConfigInit
import co.touchlab.kermit.platformLogWriter
import eu.vendeli.tgbot.interfaces.helper.Logger
import eu.vendeli.tgbot.types.internal.LogLvl

private val kermitLogger by lazy {
    co.touchlab.kermit.Logger(
        mutableLoggerConfigInit(platformLogWriter(DefaultFormatter)),
        DEFAULT_LOGGING_TAG,
    )
}

private fun LogLvl.toSeverity(): Severity = when (this) {
    LogLvl.TRACE -> Severity.Verbose
    LogLvl.DEBUG -> Severity.Debug
    LogLvl.INFO -> Severity.Info
    LogLvl.WARN -> Severity.Warn
    LogLvl.ERROR -> Severity.Error
    LogLvl.ALL -> Severity.Verbose
    LogLvl.OFF -> error("Turned off.")
}

internal actual val DEFAULT_LOGGER: Logger
    get() = Logger { lvl, tag, message, throwable ->
        kermitLogger.log(lvl.toSeverity(), tag ?: DEFAULT_LOGGING_TAG, throwable, message)
    }
