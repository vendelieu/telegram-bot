package eu.vendeli.tgbot

import eu.vendeli.tgbot.core.EnvConfigLoader
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.core.TelegramActionsCollector.collect
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.interfaces.Autowiring
import eu.vendeli.tgbot.interfaces.ConfigLoader
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.RESPONSE_UPDATES_LIST_TYPEREF
import eu.vendeli.tgbot.utils.getConfiguredHttpClient
import eu.vendeli.tgbot.utils.getConfiguredMapper
import eu.vendeli.tgbot.utils.level
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import mu.KLogging

/**
 * Telegram bot main instance
 *
 * @property token Token of your bot
 *
 * @param commandsPackage The place where the search for commands and inputs will be done.
 * @param botConfiguration Lambda function to customize the bots instance. Watch [BotConfiguration]
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
    constructor(configLoader: ConfigLoader = EnvConfigLoader) :
        this(configLoader.token, configLoader.commandsPackage) {
        config.apply(configLoader.load())
    }

    internal val config = BotConfiguration().apply(botConfiguration)

    /**
     * A tool for managing input waiting.
     */
    val inputListener get() = config.inputListener

    /**
     * A tool for managing User context.
     */
    val userData get() = config.context.userData

    /**
     * A tool for managing Chat context.
     */
    val chatData get() = config.context.chatData

    init {
        logger("eu.vendeli.tgbot").level = config.logging.botLogLevel
    }

    internal val autowiringObjects by lazy { mutableMapOf<Class<*>, Autowiring<*>>() }

    /**
     * Current bot [TelegramUpdateHandler] instance
     */
    val update = TelegramUpdateHandler(
        actions = commandsPackage?.let { collect(it) },
        bot = this,
        classManager = config.classManager,
        inputListener = config.inputListener,
    )

    internal var httpClient = getConfiguredHttpClient()

    /**
     * Gives the ability to expand magical objects
     *
     * @param clazz The class in the final method that will return
     * @param autowiring Implementation of the [Autowiring] interface to be able to generate more contextual object.
     */
    fun <T> addAutowiringObject(clazz: Class<T>, autowiring: () -> Autowiring<T>) =
        autowiringObjects.put(clazz, autowiring())

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
    suspend fun handleUpdates() {
        update.setListener {
            handle(it)
        }
    }

    /**
     * Function for processing updates by long-pulling using manual handling.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     *
     * @param block [ManualHandlingDsl]
     */
    suspend fun handleUpdates(block: ManualHandlingBlock) {
        update.setListener {
            handle(it, block)
        }
    }

    internal suspend fun pullUpdates(offset: Int? = null): List<Update>? {
        logger.debug { "Pulling updates." }
        val request = httpClient.post(
            TgMethod("getUpdates").getUrl(config.apiHost, token) + (offset?.let { "?offset=$it" } ?: ""),
        )
        return mapper.readValue(request.bodyAsText(), RESPONSE_UPDATES_LIST_TYPEREF).getOrNull()
    }

    internal companion object : KLogging() {
        internal val mapper = getConfiguredMapper()
    }
}
