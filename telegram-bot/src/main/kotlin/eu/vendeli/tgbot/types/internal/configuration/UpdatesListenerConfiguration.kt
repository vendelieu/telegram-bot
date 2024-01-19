package eu.vendeli.tgbot.types.internal.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * A class containing configurations related to updates pulling.
 *
 * @property dispatcher Dispatcher used for processing incoming updates.
 * @property pullingDelay Delay after each pulling request.
 * @property isHandlerSupervised Is handler coroutine supervised.
 * @property updatesPollingTimeout timeout option in getUpdates request for long-polling mechanism.
 */
data class UpdatesListenerConfiguration(
    var dispatcher: CoroutineDispatcher = Dispatchers.Default,
    var pullingDelay: Long = 0,
    var isHandlerSupervised: Boolean = false,
    var updatesPollingTimeout: Int = 20,
)
