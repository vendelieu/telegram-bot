package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.LogLvl
import kotlinx.serialization.Serializable

/**
 * The class containing the logging configuration.
 *
 * @property botLogLevel The level of logs of the bot actions.
 * @property httpLogLevel The level of http request logs.
 */
@Serializable
data class LoggingConfiguration(
    var botLogLevel: LogLvl = LogLvl.INFO,
    var httpLogLevel: HttpLogLevel = HttpLogLevel.NONE,
)
