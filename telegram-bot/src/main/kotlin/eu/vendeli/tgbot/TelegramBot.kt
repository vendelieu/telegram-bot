package eu.vendeli.tgbot

import eu.vendeli.tgbot.core.CodegenUpdateHandler
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.implementations.EnvConfigLoaderImpl
import eu.vendeli.tgbot.interfaces.ConfigLoader
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.configuration.BotConfiguration
import eu.vendeli.tgbot.types.media.File
import eu.vendeli.tgbot.utils.BotConfigurator
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.asClass
import eu.vendeli.tgbot.utils.getActions
import eu.vendeli.tgbot.utils.getConfiguredHttpClient
import eu.vendeli.tgbot.utils.getConfiguredMapper
import eu.vendeli.tgbot.utils.level
import io.ktor.client.request.get
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
    constructor(configLoader: ConfigLoader = EnvConfigLoaderImpl) : this(
        configLoader.token,
        configLoader.commandsPackage,
    ) {
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
        logger("eu.vendeli.tgbot").level = config.logging.botLogLevel.logbackLvl
    }

    /**
     * Current bot UpdateHandler instance
     */
    val update by lazy {
        val postFix = commandsPackage?.let { "_$it".replace(".", "_") } ?: ""
        val codegenActions = "eu.vendeli.tgbot.ActionsDataKt".asClass().getActions()
            ?: "eu.vendeli.tgbot.ActionsData${postFix}Kt".asClass().getActions(postFix)
            ?: error("Not found generated actions, check if ksp plugin is connected correctly.")

        CodegenUpdateHandler(
            codegenActions,
            this,
        )
    }

    internal var httpClient = getConfiguredHttpClient()

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
     * Function for processing updates by long-pulling using manual handling.
     *
     * Note that when using this method, other processing will be interrupted and reassigned.
     *
     * @param block [ManualHandlingDsl]
     */
    suspend fun handleUpdates(allowedUpdates: List<UpdateType>? = null, block: ManualHandlingBlock) {
        update.setListener(allowedUpdates) {
            handle(it, block)
        }
    }

    internal companion object : KLogging() {
        internal val mapper = getConfiguredMapper()
    }
}
