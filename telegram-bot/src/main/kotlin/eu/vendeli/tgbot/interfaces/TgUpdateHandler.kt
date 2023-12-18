package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.core.ManualHandlingDsl
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.NewCoroutineContext
import eu.vendeli.tgbot.utils.process
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KLogging
import kotlin.coroutines.coroutineContext

abstract class TgUpdateHandler(
    internal val bot: TelegramBot,
    private val inputListener: InputListener,
) {
    private lateinit var handlingBehaviour: HandlingBehaviourBlock

    @Volatile
    private var handlerActive: Boolean = false
    private val manualHandlingBehavior by lazy { ManualHandlingDsl(bot, inputListener) }
    val caughtExceptions by lazy { Channel<Pair<Throwable, Update>>(Channel.CONFLATED) }

    /**
     * Function that starts the listening event.
     *
     * @param offset
     */
    private tailrec suspend fun runListener(offset: Int? = null): Int = with(bot.config.updatesListener) {
        logger.debug { "Running listener with offset - $offset" }
        if (!handlerActive) {
            coroutineContext.cancelChildren()
            return 0
        }
        var lastUpdateId: Int = offset ?: 0
        bot.pullUpdates(offset)?.forEach { update ->
            NewCoroutineContext(coroutineContext + dispatcher).launch {
                handlingBehaviour(this@TgUpdateHandler, update)
            }
            lastUpdateId = update.updateId + 1
        }
        delay(pullingDelay)
        return runListener(lastUpdateId)
    }

    /**
     * Function to define the actions that will be applied to updates when they are being processed.
     * When set, it starts an update processing cycle.
     *
     * @param block action that will be applied.
     */
    suspend fun setListener(block: HandlingBehaviourBlock) {
        if (handlerActive) stopListener()
        logger.debug { "The listener is set." }
        handlingBehaviour = block
        handlerActive = true
        runListener()
    }

    /**
     * Stops listening of new updates.
     *
     */
    fun stopListener() {
        logger.debug { "The listener is stopped." }
        handlerActive = false
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
     * @return null on success or [Throwable].
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
