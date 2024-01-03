package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.newCoroutineCtx
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
    private lateinit var handlingBehaviour: HandlingBehaviourBlock
    private val updatesChannel = Channel<Update>()
    private var handlerCtx: CoroutineContext? = null
    private val manualHandlingBehavior by lazy { ManualHandlingDsl(bot) }

    /**
     * The channel where errors caught during update processing are stored with update that caused them.
     */
    val caughtExceptions by lazy { Channel<FailedUpdate>(Channel.CONFLATED) }

    private suspend fun collectUpdates() = bot.config.updatesListener.run {
        logger.debug { "Starting updates collector." }
        newCoroutineCtx((handlerCtx ?: return@run) + dispatcher).launch {
            var lastUpdateId = 0
            while (isActive) {
                logger.debug { "Running listener with offset - $lastUpdateId" }
                bot.pullUpdates(lastUpdateId)?.forEach {
                    updatesChannel.send(it)
                    lastUpdateId = it.updateId + 1
                }
                if (pullingDelay > 0) delay(pullingDelay)
            }
        }
    }

    private suspend fun processUpdates() {
        logger.info { "Starting long-polling listener." }
        newCoroutineCtx((handlerCtx ?: return) + Dispatchers.IO).launch {
            for (update in updatesChannel) {
                handlingBehaviour(this@TgUpdateHandler, update)
            }
        }.join()
    }

    /**
     * Function to define the actions that will be applied to updates when they are being processed.
     * When set, it starts an update processing cycle.
     *
     * @param block action that will be applied.
     */
    suspend fun setListener(block: HandlingBehaviourBlock) {
        if (handlerCtx?.isActive == true) stopListener()
        handlerCtx = currentCoroutineContext()
        logger.debug { "The listener is set." }
        handlingBehaviour = block
        collectUpdates()
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
    fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.debug { "Handling behaviour is set." }
        handlingBehaviour = block
    }

    /**
     * A method for handling updates from a string.
     * Define processing behaviour before calling, see [setBehaviour].
     */
    suspend fun parseAndHandle(update: String) {
        logger.debug { "Trying to parse update from string - $update" }
        mapper.runCatching {
            readValue(update, Update::class.java)
        }.onFailure {
            logger.debug(it) { "error during the update parsing process." }
        }.onSuccess { logger.info { "Successfully parsed update to $it" } }
            .getOrNull()?.let { handlingBehaviour(this, it) }
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
