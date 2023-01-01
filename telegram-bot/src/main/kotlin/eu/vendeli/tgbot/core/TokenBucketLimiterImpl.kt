package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.interfaces.RateLimitMechanism
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

/**
 * A class that stores data about limits.
 *
 * @property remainingTokens Remaining number of requests.
 * @property lastUpdated Last update.
 */
data class BucketState(
    val remainingTokens: Long,
    val lastUpdated: Instant,
)

/**
 * Default implementation of query limitation via [Token bucket](https://en.wikipedia.org/wiki/Token_bucket) algorithm.
 */
open class TokenBucketLimiterImpl : RateLimitMechanism {
    private val state: ConcurrentHashMap<String, AtomicReference<BucketState>> = ConcurrentHashMap()
    private val instant: Instant get() { return Instant.now() }

    private suspend fun compareAndSet(key: String, compareAndSetFunction: (current: BucketState?) -> BucketState) {
        val currentState = state[key]
        val currentStateValue = currentState?.get()
        val newStateValue = compareAndSetFunction(currentStateValue)

        val putResult = state.putIfAbsent(key, AtomicReference(newStateValue))

        // Check if an item was added or updated after currentState read
        if ((currentState == null && putResult != null) || currentState?.compareAndSet(
                currentStateValue,
                newStateValue,
            ) == false
        ) {
            delay(100.milliseconds)
            return compareAndSet(key, compareAndSetFunction)
        }
    }

    override suspend fun isLimited(limits: RateLimits, telegramId: Long, actionId: String?): Boolean {
        val storageKey = if (actionId == null) "$telegramId" else "$telegramId-$actionId"
        var hasTokens = true
        compareAndSet(storageKey) { current ->
            if (current == null) {
                BucketState(limits.rate - 1, instant)
            } else {
                val now = instant
                val tokensToAdd = current.lastUpdated.until(now, ChronoUnit.MILLIS) / limits.period
                val totalTokens = min(limits.rate, current.remainingTokens + tokensToAdd)
                val lastUpdated = if (tokensToAdd > 0) now else current.lastUpdated
                hasTokens = totalTokens > 0

                current.copy(
                    remainingTokens = max(0, totalTokens - 1),
                    lastUpdated = lastUpdated,
                )
            }
        }

        return !hasTokens
    }
}
