package eu.vendeli.tgbot.utils

import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.mutableLoggerConfigInit
import co.touchlab.kermit.platformLogWriter
import eu.vendeli.tgbot.types.internal.LogLvl

internal open class KermitLogger(
    id: String,
) : Logger(id) {
    private val logger = co.touchlab.kermit.Logger(
        mutableLoggerConfigInit(platformLogWriter(DefaultFormatter), minSeverity = Severity.Info),
        id,
    )

    private var lvl = Severity.Info

    override fun setLevel(level: LogLvl) {
        lvl = when (level) {
            LogLvl.OFF -> throw NotImplementedError()
            LogLvl.ERROR -> Severity.Error
            LogLvl.WARN -> Severity.Warn
            LogLvl.INFO -> Severity.Info
            LogLvl.DEBUG -> Severity.Debug
            LogLvl.TRACE -> Severity.Verbose
            LogLvl.ALL -> Severity.Verbose
        }
        logger.mutableConfig.minSeverity = lvl
    }

    override fun info(message: () -> String) = logger.i(message = message)
    override fun warn(message: () -> String) = logger.w(message = message)
    override fun debug(message: () -> String) = logger.d(message = message)
    override fun trace(message: () -> String) = logger.v(message = message)
    override fun error(throwable: Throwable?, message: () -> String) =
        logger.e(throwable = throwable, message = message)
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual open class Logging actual constructor(
    tag: String,
) {
    actual val logger: Logger = KermitLogger(tag)
}
