package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.types.internal.HttpLogLevel
import java.lang.System.Logger.Level

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
