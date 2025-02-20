package eu.vendeli.tgbot.types.configuration

import eu.vendeli.tgbot.annotations.dsl.ConfigurationDSL
import eu.vendeli.tgbot.types.component.HttpLogLevel
import eu.vendeli.tgbot.types.component.LogLvl
import kotlinx.serialization.Serializable

/**
 * The class containing the logging configuration.
 *
 * @property botLogLevel The level of logs of the bot actions.
 * @property httpLogLevel The level of http request logs.
 */
@Serializable
@ConfigurationDSL
data class LoggingConfiguration(
    var botLogLevel: LogLvl = LogLvl.INFO,
    var httpLogLevel: HttpLogLevel = HttpLogLevel.NONE,
)
