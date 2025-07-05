package eu.vendeli.tgbot.implementations

import co.touchlab.stately.concurrency.AtomicReference
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.interfaces.helper.RateLimitMechanism
import eu.vendeli.tgbot.types.configuration.RateLimits
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

/**
 * Default implementation of query limitation via [Token bucket](https://en.wikipedia.org/wiki/Token_bucket) algorithm.
 */
class TokenBucketLimiterImpl : RateLimitMechanism {
    private var state: ConcurrentMap<String, AtomicReference<BucketState>> = ConcurrentMap()
    private val instant: Instant
        get() {
            return Clock.System.now()
        }

    /**
     * A class that stores data about limits.
     *
     * @property remainingTokens Remaining number of requests.
     * @property lastUpdated Last update.
     */
    private data class BucketState(
        val remainingTokens: Long,
        val lastUpdated: Instant,
    )

    @KtGramInternal
    fun resetState() {
        state = ConcurrentMap()
    }

    private suspend fun compareAndSet(key: String, compareAndSetFunction: (current: BucketState?) -> BucketState) {
        val currentState = state[key]
        val currentStateValue = currentState?.get()
        val newStateValue = compareAndSetFunction(currentStateValue)

        val putResult = state[key]?.let { null } ?: state.put(key, AtomicReference(newStateValue))

        // Check if an item was added or updated after currentState read
        if ((currentState == null && putResult != null) ||
            currentState?.compareAndSet(
                currentStateValue!!,
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
                val passedMillis = current.lastUpdated.toEpochMilliseconds() - now.toEpochMilliseconds()
                val tokensToAdd = passedMillis / limits.period
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
