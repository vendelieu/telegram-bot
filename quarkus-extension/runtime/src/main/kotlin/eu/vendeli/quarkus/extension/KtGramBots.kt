package eu.vendeli.quarkus.extension

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.utils.common.fqName
import io.ktor.client.HttpClient
import io.ktor.util.logging.KtorSimpleLogger
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.event.Observes
import jakarta.enterprise.inject.Instance
import jakarta.enterprise.inject.Produces
import jakarta.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Creates the configured bots, exposes them as a `List<TelegramBot>` bean
 * and starts long polling on application startup.
 */
@Singleton
class KtGramBots(
    private val config: KtGramConfig,
    private val classManager: QuarkusClassManager,
    private val configurations: Instance<BotConfiguration>,
    private val httpClient: Instance<HttpClient>,
) {
    private val logger = KtorSimpleLogger(this::class.fqName)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + CoroutineName("ktgram"))
    private val bots: List<TelegramBot> by lazy(::createBots)

    @Produces
    @Singleton
    fun botInstances(): List<TelegramBot> = bots

    /** The bot of a single-bot setup; with several bots, inject `List<TelegramBot>` instead. */
    @Produces
    @Singleton
    fun botInstance(): TelegramBot = bots.singleOrNull()
        ?: error("${bots.size} bots are configured, inject List<TelegramBot> instead of TelegramBot")

    fun onStart(
        @Observes event: StartupEvent,
    ) {
        for (bot in bots) {
            val botCfg = botConfigurationFor(bot.identifier)

            if (botCfg?.autostartLongPolling == false || !config.autoStartPolling()) {
                logger.warn("Bot ${bot.identifier} is not started automatically, make sure you handle this manually")
                continue
            }
            scope.launch {
                botCfg?.onInit(bot)
                bot.handleUpdatesCatching(botCfg)
            }
        }
    }

    /** Cancelling the scope cancels the running `handleUpdates` calls, which stops their listeners. */
    fun onStop(
        @Observes event: ShutdownEvent,
    ) = scope.cancel()

    private fun createBots(): List<TelegramBot> {
        val sharedClient = if (config.shareHttpClient() && httpClient.isResolvable) httpClient.get() else null

        return config.bot().map { bot ->
            TelegramBot(bot.token(), bot.pckg().orElse(null), sharedClient) {
                classManager = this@KtGramBots.classManager
                identifier = bot.identifier()
                botConfigurationFor(bot.identifier())?.let { apply(it.applyCfg()) }
            }
        }
    }

    private fun botConfigurationFor(identifier: String): BotConfiguration? =
        configurations.firstOrNull { it.identifier == identifier }

    /**
     * Transient polling errors are already retried inside `handleUpdates`;
     * this restarts it after the failures it gives up on, as the Spring starter does.
     */
    private suspend fun TelegramBot.handleUpdatesCatching(botCfg: BotConfiguration?) {
        for (attempt in 1 until config.maxHandlingRetries()) {
            if (attempt > 1) logger.warn("Retrying to handle updates, attempt $attempt")
            try {
                return handleUpdates(botCfg?.allowedUpdates)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                logger.error("An error occurred while handling updates", e)
                botCfg?.onHandlerException(e)
            }
        }
        logger.error("Max handling retries reached")
    }
}
