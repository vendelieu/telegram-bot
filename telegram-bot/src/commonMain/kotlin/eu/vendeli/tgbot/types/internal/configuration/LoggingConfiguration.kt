package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.interfaces.helper.Logger
import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.LogLvl
import eu.vendeli.tgbot.utils.DEFAULT_LOGGER
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The class containing the logging configuration.
 *
 * @property logger Logger which will be used for logging.
 * @property botLogLevel The level of logs of the bot actions.
 * @property httpLogLevel The level of http request logs.
 */
@Serializable
data class LoggingConfiguration(
    @Transient
    var logger: Logger = DEFAULT_LOGGER,
    var botLogLevel: LogLvl = LogLvl.INFO,
    var httpLogLevel: HttpLogLevel = HttpLogLevel.NONE,
)
