package eu.vendeli.tgbot.types.internal.configuration

import eu.vendeli.tgbot.utils.PROCESSING_DISPATCHER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * A class containing configurations related to updates pulling.
 *
 * @property dispatcher Dispatcher used for collecting incoming updates.
 * @property processingDispatcher Dispatcher that is used to process updates.
 * Default is [Dispatchers.IO]/[Dispatchers.Unconfined], depends on a platform.
 * @property pullingDelay Delay after each pulling request.
 * @property updatesPollingTimeout timeout option in getUpdates request for a long-polling mechanism.
 */
data class UpdatesListenerConfiguration(
    var dispatcher: CoroutineDispatcher = Dispatchers.Default,
    var processingDispatcher: CoroutineDispatcher = PROCESSING_DISPATCHER,
    var pullingDelay: Long = 0,
    var updatesPollingTimeout: Int = 20,
)
