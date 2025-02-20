package eu.vendeli.tgbot.types.component

import io.ktor.client.plugins.logging.LogLevel

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
}

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
