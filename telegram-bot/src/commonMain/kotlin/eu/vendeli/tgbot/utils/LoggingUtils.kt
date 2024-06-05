package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.types.internal.LogLvl

abstract class Logger(val id: String) : io.ktor.client.plugins.logging.Logger {
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

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect open class Logging(tag: String = "eu.vendeli.TelegramBot") {
    val logger: Logger
}
