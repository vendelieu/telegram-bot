package eu.vendeli.tgbot.types.internal

import io.ktor.client.plugins.logging.LogLevel

/**
 * Proxy enum for Bot logger leveling.
 */
enum class LogLvl(
    val int: Int,
) {
    OFF(Int.MAX_VALUE),
    ERROR(40000),
    WARN(30000),
    INFO(20000),
    DEBUG(10000),
    TRACE(5000),
    ALL(Int.MIN_VALUE),
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
