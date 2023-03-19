package eu.vendeli.tgbot.core

import ch.qos.logback.classic.Level
import eu.vendeli.tgbot.interfaces.BotChatData
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.interfaces.BotUserData
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.ConfigLoader
import eu.vendeli.tgbot.interfaces.RateLimitMechanism
import eu.vendeli.tgbot.types.internal.HttpLogLevel
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration

object EnvConfigLoader : ConfigLoader {
    private const val PREFIX = "TGBOT_"
    internal var envVars = System.getenv().filter { it.key.startsWith(PREFIX) }

    override val token: String by lazy {
        getVal("TOKEN") ?: throw NullPointerException("Env parameter ${PREFIX}TOKEN not found")
    }

    override val commandsPackage: String? = getVal("COMMANDS_PACKAGE")

    private fun getVal(value: String) = envVars[PREFIX + value]
    private fun String.init() = Class.forName(this).constructors.first().newInstance()
    private fun apply2Config(block: BotConfiguration.() -> Unit) = BotConfiguration().also(block)

    @Suppress("CyclomaticComplexMethod")
    override fun load(): BotConfiguration = apply2Config {
        // general
        getVal("API_HOST")?.also { apiHost = it }
        getVal("INPUT_LISTENER")?.also { inputListener = it.init() as BotInputListener }
        getVal("CLASS_MANAGER")?.also { classManager = it.init() as ClassManager }
        getVal("RATE_LIMITER")?.also { rateLimiter = it.init() as RateLimitMechanism }
        // httpClient
        getVal("HTTPC_RQ_TIMEOUT_MILLIS")?.toLongOrNull()?.also { httpClient.requestTimeoutMillis = it }
        getVal("HTTPC_C_TIMEOUT_MILLIS")?.toLongOrNull()?.also { httpClient.connectTimeoutMillis = it }
        getVal("HTTPC_SOC_TIMEOUT_MILLIS")?.toLongOrNull()?.also { httpClient.socketTimeoutMillis = it }
        getVal("HTTPC_MAX_RQ_RETRY")?.toIntOrNull()?.also { httpClient.maxRequestRetry = it }
        getVal("HTTPC_RETRY_DELAY")?.toLongOrNull()?.also { httpClient.retryDelay = it }
        // logging
        getVal("LOG_BOT_LVL")?.also { logging.botLogLevel = Level.toLevel(it) }
        getVal("LOG_HTTP_LVL")?.also { logging.httpLogLevel = HttpLogLevel.valueOf(it) }
        // rateLimits
        getVal("RATES_PERIOD")?.toLongOrNull()?.also { rateLimits.period = it }
        getVal("RATES_RATE")?.toLongOrNull()?.also { rateLimits.rate = it }
        // context
        getVal("CTX_USER_DATA")?.also { context.userData = it.init() as BotUserData }
        getVal("CTX_CHAT_DATA")?.also { context.chatData = it.init() as BotChatData }
        // commandParsing
        getVal("CMDPRS_CMD_DELIMITER")?.firstOrNull()?.also { commandParsing.commandDelimiter = it }
        getVal("CMDPRS_PARAMS_DELIMITER")?.firstOrNull()?.also { commandParsing.parametersDelimiter = it }
        getVal("CMDPRS_PARAMVAL_DELIMITER")?.firstOrNull()?.also {
            commandParsing.parameterValueDelimiter = it
        }
        getVal("CMDPRS_RESTRICT_SPC_INCMD")?.toBooleanStrictOrNull()?.also {
            commandParsing.restrictSpacesInCommands = it
        }
    }
}
