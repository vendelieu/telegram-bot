package eu.vendeli.tgbot.types.internal.configuration

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.types.HttpLogLevel

/**
 * The class containing the logging configuration.
 *
 * @property botLogLevel The level of logs of the bot actions.
 * @property httpLogLevel The level of http request logs.
 */
data class LoggingConfiguration(
    var botLogLevel: Level = Level.INFO,
    var httpLogLevel: HttpLogLevel = HttpLogLevel.NONE,
)