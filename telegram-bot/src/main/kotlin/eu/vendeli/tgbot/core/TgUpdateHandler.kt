package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.utils.GET_UPDATES_ACTION
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.launchInCtx
import eu.vendeli.tgbot.utils.launchInNewCtx
import eu.vendeli.tgbot.utils.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KLogging
import kotlin.coroutines.CoroutineContext

/**
 * A basic update processing class that is extends by a [CodegenUpdateHandler].
 *
 * @property bot bot instance.
 */
abstract class TgUpdateHandler internal constructor(
    internal val bot: TelegramBot,
) {
    private var handlingBehaviour: HandlingBehaviourBlock = { handle(it) }
    private val updatesChannel = Channel<Update>()
    private var handlerCtx: CoroutineContext? = null
    private val manualHandlingBehavior by lazy { ManualHandlingDsl(bot) }

    /**
     * The channel where errors caught during update processing are stored with update that caused them.
     */
    val caughtExceptions by lazy { Channel<FailedUpdate>(Channel.CONFLATED) }

    private suspend fun collectUpdates(types: List<UpdateType>?) {
        logger.debug { "Starting updates collector." }
        launchInNewCtx((handlerCtx ?: return)) {
            var lastUpdateId = 0
            while (isActive) {
                logger.debug { "Running listener with offset - $lastUpdateId" }
                GET_UPDATES_ACTION.options {
                    offset = lastUpdateId
                    allowedUpdates = types
                }.sendAsync(bot).getOrNull()?.forEach {
                    updatesChannel.send(it)
                    lastUpdateId = it.updateId + 1
                }
                bot.config.updatesListener.pullingDelay.takeIf { it > 0 }?.let { delay(it) }
            }
        }
    }

    private suspend fun processUpdates() {
        logger.info { "Starting long-polling listener." }
        launchInNewCtx((handlerCtx ?: return)) {
            for (update in updatesChannel) {
                launch(Dispatchers.IO) { handlingBehaviour(this@TgUpdateHandler, update) }
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
        if (handlerCtx?.isActive == true) stopListener()
        handlerCtx = currentCoroutineContext() + bot.config.updatesListener.dispatcher
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
        handlerCtx?.cancelChildren()
        handlerCtx = null
        logger.debug { "The listener is stopped." }
    }

    /**
     * A function for defining the behavior to handle updates.
     */
    suspend fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.debug { "Handling behaviour is set." }
        if (handlerCtx?.isActive == true) stopListener()
        handlingBehaviour = block
        handlerCtx = currentCoroutineContext() + bot.config.updatesListener.dispatcher
    }

    /**
     * A method for handling updates from a string.
     * Define processing behaviour before calling, see [setBehaviour].
     */
    suspend fun parseAndHandle(update: String) {
        logger.debug { "Trying to parse update from string - $update" }
        mapper.runCatching { readValue(update, Update::class.java) }.onFailure {
            logger.debug(it) { "error during the update parsing process." }
        }.onSuccess { logger.info { "Successfully parsed update to $it" } }.getOrNull()?.let {
            logger.debug { "Processing update with preset behaviour." }
            launchInCtx((handlerCtx ?: return@let) + Dispatchers.IO) {
                handlingBehaviour(this@TgUpdateHandler, it)
            }
        }
    }

    /**
     * Handle the update.
     *
     * @param update
     */
    abstract suspend fun handle(update: Update)

    /**
     * Manual handling dsl
     *
     * @param update
     * @param block
     */
    suspend fun handle(update: Update, block: ManualHandlingBlock) {
        logger.debug { "Manually handling update: $update" }
        manualHandlingBehavior.apply {
            block()
            process(update)
        }
    }

    internal companion object : KLogging()
}
