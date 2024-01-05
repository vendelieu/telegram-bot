package eu.vendeli.tgbot.types.internal

import ch.qos.logback.classic.Level
import io.ktor.client.plugins.logging.LogLevel

/**
 * Proxy enum class for Ktor log leveling.
 *
 */
enum class HttpLogLevel {
    ALL,
    HEADERS,
    BODY,
    INFO,
    NONE,
    ;

    internal fun toKtorLvl() = when (this) {
        ALL -> LogLevel.ALL
        HEADERS -> LogLevel.HEADERS
        BODY -> LogLevel.BODY
        INFO -> LogLevel.INFO
        NONE -> LogLevel.NONE
    }
}

/**
 * Proxy enum for Bot logger leveling.
 */
enum class LogLvl {
    OFF,
    ERROR,
    WARN,
    INFO,
    DEBUG,
    TRACE,
    ALL,
    ;

    internal val logbackLvl
        get(): Level = when (this) {
            OFF -> Level.OFF
            ERROR -> Level.ERROR
            WARN -> Level.WARN
            INFO -> Level.INFO
            DEBUG -> Level.DEBUG
            TRACE -> Level.TRACE
            ALL -> Level.ALL
        }
}
