package eu.vendeli.tgbot

import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.helper.ConfigLoader
import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.types.configuration.BotConfiguration
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.common.BotConfigurator
import eu.vendeli.tgbot.utils.common.DEFAULT_HANDLING_BEHAVIOUR
import eu.vendeli.tgbot.utils.common.FunctionalHandlingBlock
import eu.vendeli.tgbot.utils.common.fqName
import eu.vendeli.tgbot.utils.common.getConfiguredHttpClient
import eu.vendeli.tgbot.utils.internal.getLogger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.SupervisorJob

/**
 * Telegram bot main instance
 *
 * @property token Token of your bot
 *
 * @param commandsPackage The place where the search for commands and inputs will be done.
 * @param botConfiguration Lambda function to customize the bots instance. See [BotConfiguration]
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class TelegramBot(
    internal val token: String,
    commandsPackage: String? = null,
    botConfiguration: BotConfigurator = {},
) {
    /**
     * Constructor to build through configuration loader.
     */
    constructor(configLoader: ConfigLoader) : this(
        configLoader.token,
        configLoader.commandsPackage,
    ) {
        config.apply(configLoader.load())
    }

    constructor(
        token: String,
        commandsPackage: String? = null,
        httpClient: HttpClient? = null,
        botConfiguration: BotConfigurator = {},
    ) : this(token, commandsPackage, botConfiguration) {
        this.httpClient = httpClient ?: getConfiguredHttpClient(config.httpClient, config.logging)
    }

    internal val rootJob = SupervisorJob()
    internal val config = BotConfiguration().apply(botConfiguration)
    internal val logger = getLogger(config.logging.botLogLevel, this::class.fqName)

    internal val baseUrl by lazy { "${config.apiHost}/bot$token" + if (config.isTestEnv) "/test/" else "/" }

    /**
     * A tool for managing input waiting.
     */
    val inputListener get() = config.inputListener

    /**
     * Bot identifier set through configuration.
     */
    val identifier: String get() = config.identifier

    /**
     * Current bot UpdateHandler instance
     */
    val update = TgUpdateHandler(commandsPackage, this)

    internal var httpClient = getConfiguredHttpClient(config.httpClient, config.logging)

    init {
        logger.debug("[$identifier] Ktor using engine: ${httpClient.engine::class.simpleName}")
    }

    /**
     * Get direct url from [File] if [File.filePath] is present
     *
     * @param file
     * @return direct url to file
     */
    fun getFileDirectUrl(file: File): String? = file.getDirectUrl(config.apiHost, token)

    /**
     * Get file from [File] if [File.filePath] is present.
     *
     * @param file
     * @return [ByteArray]
     */
    suspend fun getFileContent(file: File): ByteArray? =
        file.getDirectUrl(config.apiHost, token)?.let { httpClient.get(it).readRawBytes() }

    /**
     * Function for processing updates by long-pulling using annotation commands.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     */
    suspend fun handleUpdates(allowedUpdates: List<UpdateType>? = null) {
        update.setListener(allowedUpdates, DEFAULT_HANDLING_BEHAVIOUR)
    }

    /**
     * Function for processing updates by long-pulling using functional handling.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     *
     * @param block [FunctionalHandlingDsl]
     */
    suspend fun handleUpdates(allowedUpdates: List<UpdateType>? = null, block: FunctionalHandlingBlock) {
        update.setListener(allowedUpdates) {
            handle(it, block)
        }
    }

    companion object
}
