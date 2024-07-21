package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.DEFAULT_HANDLING_BEHAVIOUR
import eu.vendeli.tgbot.utils.FunctionalHandlingBlock
import eu.vendeli.tgbot.utils.GET_UPDATES_ACTION
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.Logging
import eu.vendeli.tgbot.utils.coHandle
import eu.vendeli.tgbot.utils.process
import eu.vendeli.tgbot.utils.serde
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * A basic update processing class that is extended by a [CodegenUpdateHandler].
 *
 * @property bot bot instance.
 */
abstract class TgUpdateHandler internal constructor(
    internal val bot: TelegramBot,
) {
    private var handlingBehaviour: HandlingBehaviourBlock = DEFAULT_HANDLING_BEHAVIOUR
    private val updatesChannel = Channel<ProcessedUpdate>()
    internal val handlerScope = bot.config.updatesListener.run {
        CoroutineScope(dispatcher + CoroutineName("TgBot"))
    }
    internal val functionalHandlingBehavior by lazy { FunctionalHandlingDsl(bot) }

    /**
     * The channel where errors caught during update processing are stored with update that caused them.
     */
    val caughtExceptions by lazy { Channel<FailedUpdate>(Channel.CONFLATED) }

    private suspend fun collectUpdates(types: List<UpdateType>?) = bot.config.updatesListener.run {
        logger.debug { "Starting updates collector." }
        coHandle {
            var lastUpdateId = 0
            while (isActive) {
                logger.debug { "Running listener with offset - $lastUpdateId" }
                GET_UPDATES_ACTION
                    .options {
                        offset = lastUpdateId
                        allowedUpdates = types
                        timeout = updatesPollingTimeout
                    }.sendAsync(bot)
                    .getOrNull()
                    ?.forEach {
                        updatesChannel.send(it)
                        lastUpdateId = it.updateId + 1
                    }
                pullingDelay.takeIf { it > 0 }?.let { delay(it) }
            }
        }
    }

    private suspend fun processUpdates() {
        logger.info { "Starting long-polling listener." }
        coHandle {
            for (update in updatesChannel) {
                launch(bot.config.updatesListener.processingDispatcher) {
                    handlingBehaviour(this@TgUpdateHandler, update)
                }
            }
        }.join()
    }

    /**
     * Function to define the actions that will be applied to updates when they are being processed.
     * When set, it starts an update processing cycle.
     *
     * @param block action that will be applied.
     */
    suspend fun setListener(allowedUpdates: List<UpdateType>? = null, block: HandlingBehaviourBlock) {
        stopListener()
        logger.debug { "The listener is set." }
        handlingBehaviour = block
        collectUpdates(allowedUpdates)
        processUpdates()
    }

    /**
     * Stops listening of new updates.
     *
     */
    fun stopListener() {
        handlerScope.coroutineContext.cancelChildren()
        logger.debug { "The listener is stopped." }
    }

    /**
     * A function for defining the behavior to handle updates.
     */
    fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.debug { "Handling behaviour is set." }
        stopListener()
        handlingBehaviour = block
    }

    /**
     * A method for handling updates from a string.
     * Define processing behavior before calling, see [setBehaviour].
     */
    suspend fun parseAndHandle(update: String) = parse(update)
        ?.let {
            logger.debug { "Processing update with preset behaviour." }
            coHandle(
                bot.config.updatesListener.processingDispatcher,
            ) { handlingBehaviour(this@TgUpdateHandler, it) }
        }

    /**
     * A method to parse update from string.
     */
    fun parse(update: String): ProcessedUpdate? {
        logger.debug { "Trying to parse update from string - $update" }
        return serde
            .runCatching { decodeFromString(ProcessedUpdate.serializer(), update) }
            .onFailure {
                logger.error(it) { "error during the update parsing process." }
            }.onSuccess { logger.info { "Successfully parsed update to $it" } }
            .getOrNull()
    }

    /**
     * Handle the update.
     *
     * @param update
     */
    abstract suspend fun handle(update: ProcessedUpdate)

    /**
     * Functional handling dsl
     *
     * @param update
     * @param block
     */
    suspend fun handle(update: ProcessedUpdate, block: FunctionalHandlingBlock) {
        logger.debug { "Functionally handling update: $update" }
        functionalHandlingBehavior.apply {
            block()
            process(update)
        }
    }

    internal companion object : Logging("eu.vendeli.core.TgUpdateHandler")
}
