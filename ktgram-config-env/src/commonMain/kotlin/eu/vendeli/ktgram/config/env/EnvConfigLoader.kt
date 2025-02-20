package eu.vendeli.ktgram.config.env

import dev.scottpierce.envvar.EnvVar
import eu.vendeli.tgbot.interfaces.helper.ConfigLoader
import eu.vendeli.tgbot.types.component.HttpLogLevel
import eu.vendeli.tgbot.types.component.LogLvl
import eu.vendeli.tgbot.types.configuration.BotConfiguration
import eu.vendeli.tgbot.types.configuration.CommandParsingConfiguration
import eu.vendeli.tgbot.types.configuration.HttpConfiguration
import eu.vendeli.tgbot.types.configuration.LoggingConfiguration
import eu.vendeli.tgbot.types.configuration.RateLimiterConfiguration
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.types.configuration.UpdatesListenerConfiguration

const val DEFAULT_ENV_PREFIX = "KTGRAM_"

object EnvConfigLoader : ConfigLoader {
    override val token: String by lazy {
        EnvVar.require(DEFAULT_ENV_PREFIX + "TOKEN")
    }

    override val commandsPackage: String? = EnvVar[DEFAULT_ENV_PREFIX + "PACKAGE"]

    override fun load(): BotConfiguration {
        val identifier =
            EnvVar.get(DEFAULT_ENV_PREFIX + "IDENTIFIER") { "KtGram" }
        val apiHost =
            EnvVar.get(DEFAULT_ENV_PREFIX + "API_HOST") { "https://api.telegram.org" }
        val isTestEnv = EnvVar.get(DEFAULT_ENV_PREFIX + "IS_TEST_ENV") { "false" }.toBoolean()
        val inputAutoRemoval = EnvVar.get(DEFAULT_ENV_PREFIX + "INPUT_AUTO_REMOVAL") { "true" }.toBoolean()
        val throwExOnActionsFailure =
            EnvVar.get(DEFAULT_ENV_PREFIX + "THROW_EX_ON_ACTIONS_FAILURE") { "false" }.toBoolean()

        val rateLimiterConfiguration = RateLimiterConfiguration(
            limits = RateLimits(),
        )

        val httpConfiguration = HttpConfiguration(
            requestTimeoutMillis = EnvVar
                .get(
                    DEFAULT_ENV_PREFIX + "HTTP_REQUEST_TIMEOUT_MILLIS",
                ) { Int.MAX_VALUE.toString() }
                .toLong(),
            connectTimeoutMillis = EnvVar
                .get(
                    DEFAULT_ENV_PREFIX + "HTTP_CONNECT_TIMEOUT_MILLIS",
                ) { Int.MAX_VALUE.toString() }
                .toLong(),
            socketTimeoutMillis = EnvVar
                .get(
                    DEFAULT_ENV_PREFIX + "HTTP_SOCKET_TIMEOUT_MILLIS",
                ) { Int.MAX_VALUE.toString() }
                .toLong(),
            maxRequestRetry = EnvVar.get(DEFAULT_ENV_PREFIX + "HTTP_MAX_REQUEST_RETRY") { "3" }.toInt(),
            retryDelay = EnvVar.get(DEFAULT_ENV_PREFIX + "HTTP_RETRY_DELAY") { "3000" }.toLong(),
        )

        val loggingConfiguration = LoggingConfiguration(
            botLogLevel = LogLvl.valueOf(EnvVar.get(DEFAULT_ENV_PREFIX + "BOT_LOG_LEVEL") { "INFO" }),
            httpLogLevel = HttpLogLevel.valueOf(EnvVar.get(DEFAULT_ENV_PREFIX + "HTTP_LOG_LEVEL") { "NONE" }),
        )

        val updatesListenerConfiguration = UpdatesListenerConfiguration(
            pullingDelay = EnvVar.get(DEFAULT_ENV_PREFIX + "UPDATES_PULLING_DELAY") { "0" }.toLong(),
            updatesPollingTimeout = EnvVar.get(DEFAULT_ENV_PREFIX + "UPDATES_POLLING_TIMEOUT") { "20" }.toInt(),
        )

        val commandParsingConfiguration = CommandParsingConfiguration(
            commandDelimiter = EnvVar.get(DEFAULT_ENV_PREFIX + "COMMAND_DELIMITER") { "?" }[0],
            parametersDelimiter = EnvVar.get(DEFAULT_ENV_PREFIX + "PARAMETERS_DELIMITER") { "&" }[0],
            parameterValueDelimiter = EnvVar.get(DEFAULT_ENV_PREFIX + "PARAMETER_VALUE_DELIMITER") { "=" }[0],
            restrictSpacesInCommands = EnvVar
                .get(DEFAULT_ENV_PREFIX + "RESTRICT_SPACES_IN_COMMANDS") { "false" }
                .toBoolean(),
            useIdentifierInGroupCommands = EnvVar
                .get(
                    DEFAULT_ENV_PREFIX + "USE_IDENTIFIER_IN_GROUP_COMMANDS",
                ) { "false" }
                .toBoolean(),
        )

        return BotConfiguration(
            identifier,
            apiHost,
            isTestEnv,
            inputAutoRemoval = inputAutoRemoval,
            rateLimiter = rateLimiterConfiguration,
            httpClient = httpConfiguration,
            logging = loggingConfiguration,
            updatesListener = updatesListenerConfiguration,
            commandParsing = commandParsingConfiguration,
            throwExOnActionsFailure = throwExOnActionsFailure,
        )
    }
}
