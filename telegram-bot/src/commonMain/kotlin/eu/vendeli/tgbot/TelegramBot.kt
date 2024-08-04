package eu.vendeli.tgbot

import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.core.CodegenUpdateHandler
import eu.vendeli.tgbot.core.FunctionalHandlingDsl
import eu.vendeli.tgbot.core.TgUpdateHandler
import eu.vendeli.tgbot.interfaces.helper.ConfigLoader
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.FunctionalHandlingBlock
import eu.vendeli.tgbot.utils.Logging
import eu.vendeli.tgbot.utils.getConfiguredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes

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

    @OptIn(InternalApi::class)
    constructor(
        token: String,
        commandsPackage: String? = null,
        httpClient: HttpClient? = null,
        botConfiguration: BotConfigurator = {},
    ) : this(token, commandsPackage, botConfiguration) {
        this.httpClient = httpClient ?: getConfiguredHttpClient(config)
    }

    internal val config = BotConfiguration().apply(botConfiguration)

    internal val baseUrl by lazy { "${config.apiHost}/bot$token" + if (config.isTestEnv) "/test/" else "/" }

    /**
     * A tool for managing input waiting.
     */
    val inputListener get() = config.inputListener

    init {
        logger.setLevel(config.logging.botLogLevel)
    }

    /**
     * Bot identifier set through configuration.
     */
    val identifier: String get() = config.identifier

    /**
     * Current bot UpdateHandler instance
     */
    val update: TgUpdateHandler = CodegenUpdateHandler(commandsPackage, this)

    @OptIn(InternalApi::class)
    internal var httpClient = getConfiguredHttpClient(config)

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
        file.getDirectUrl(config.apiHost, token)?.let { httpClient.get(it).readBytes() }

    /**
     * Function for processing updates by long-pulling using annotation commands.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     */
    suspend fun handleUpdates(allowedUpdates: List<UpdateType>? = null) {
        update.setListener(allowedUpdates) {
            handle(it)
        }
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

    internal companion object : Logging()
}
