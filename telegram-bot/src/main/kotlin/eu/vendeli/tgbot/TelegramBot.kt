package eu.vendeli.tgbot

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.core.TelegramActionsCollector.collect
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.interfaces.MagicObject
import eu.vendeli.tgbot.types.File
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.TELEGRAM_API_URL_PATTERN
import eu.vendeli.tgbot.utils.TELEGRAM_FILE_URL_PATTERN
import eu.vendeli.tgbot.utils.getConfiguredHttpClient
import eu.vendeli.tgbot.utils.getConfiguredMapper
import eu.vendeli.tgbot.utils.level
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import mu.KotlinLogging.logger

/**
 * Telegram bot main instance
 *
 * @property token Token of your bot
 *
 * @param commandsPackage The place where the search for commands and inputs will be done.
 * @param botConfiguration Lambda function to customize the bot's instance. Watch [BotConfiguration]
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class TelegramBot(
    private val token: String,
    commandsPackage: String? = null,
    botConfiguration: BotConfigurator = {},
) {
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

    internal fun TgMethod.toUrl() = TELEGRAM_API_URL_PATTERN.format(config.apiHost, token) + name

    internal val magicObjects = mutableMapOf<Class<*>, MagicObject<*>>()

    /**
     * Current bot [TelegramUpdateHandler] instance
     */
    val update = TelegramUpdateHandler(
        actions = commandsPackage?.let { collect(it) },
        bot = this,
        classManager = config.classManager,
        inputListener = config.inputListener,
        rateLimiter = config.rateLimiter,
    )

    internal var httpClient = getConfiguredHttpClient()

    /**
     * Gives the ability to expand magical objects
     *
     * @param clazz The class in the final method that will return
     * @param magicObject Implementation of the [MagicObject] interface to be able to generate more contextual object.
     */
    fun <T> addMagicObject(clazz: Class<T>, magicObject: () -> MagicObject<T>) = magicObjects.put(clazz, magicObject())

    /**
     * Get direct url from [File] if [File.filePath] is present
     *
     * @param file
     * @return direct url to file
     */
    fun getFileDirectUrl(file: File): String? =
        if (file.filePath != null) TELEGRAM_FILE_URL_PATTERN.format(config.apiHost, token, file.filePath)
        else null

    /**
     * Get file from [File] if [File.filePath] is present.
     *
     * @param file
     * @return [ByteArray]
     */
    suspend fun getFileContent(file: File): ByteArray? = if (file.filePath != null) {
        httpClient.get(TELEGRAM_FILE_URL_PATTERN.format(config.apiHost, token, file.filePath)).readBytes()
    } else null

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
        logger.trace { "Pulling updates." }
        val request = httpClient.post(
            TgMethod("getUpdates").toUrl() + (offset?.let { "?offset=$it" } ?: ""),
        )
        return mapper.readValue(request.bodyAsText(), jacksonTypeRef<Response<List<Update>>>()).getOrNull()
    }

    internal companion object {
        internal val logger = logger {}
        internal val mapper = getConfiguredMapper()
    }
}
