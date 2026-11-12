package eu.vendeli.tgbot.mock

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration

/** One API call as the server saw it, handy for assertions in tests. */
data class RecordedCall(
    val method: String,
    val params: JsonObject,
    val status: Int,
    val response: JsonElement,
)

class StoredFile(
    val fileId: String,
    val uniqueId: String,
    val path: String,
    val name: String?,
    val mimeType: String?,
    val bytes: ByteArray?,
)

class ChatState(
    val id: Long,
    val type: String,
    title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val isForum: Boolean = false,
) {
    val messages = ConcurrentSkipListMap<Long, JsonObject>()
    val members = ConcurrentHashMap<Long, String>()
    val inviteLinks = ConcurrentHashMap<String, JsonObject>()
    val joinRequests = ConcurrentHashMap.newKeySet<Long>()
    val topics = ConcurrentHashMap<Long, JsonObject>()
    val closedTopics = ConcurrentHashMap.newKeySet<Long>()
    val reactions = ConcurrentHashMap<Long, JsonElement>()
    val customTitles = ConcurrentHashMap<Long, String>()
    val tags = ConcurrentHashMap<Long, String>()
    val readMessages = ConcurrentHashMap.newKeySet<Long>()
    val bannedSenderChats = ConcurrentHashMap.newKeySet<Long>()
    val drafts = ConcurrentHashMap<Long, JsonObject>()

    /** Ephemeral messages keyed by `receiverUserId:ephemeralMessageId`. */
    val ephemerals = ConcurrentHashMap<String, JsonObject>()
    val ephemeralCounter = AtomicLong()
    private val lastMessageId = AtomicLong()

    @Volatile
    var title: String? = title

    @Volatile
    var description: String? = null

    @Volatile
    var hasPhoto: Boolean = false

    @Volatile
    var permissions: JsonObject? = null

    @Volatile
    var stickerSetName: String? = null

    @Volatile
    var primaryInviteLink: String? = null

    @Volatile
    var pinnedMessageId: Long? = null

    @Volatile
    var generalClosed: Boolean = false

    @Volatile
    var generalHidden: Boolean = false

    @Volatile
    var generalName: String = "General"

    val isPrivate get() = type == "private"

    fun nextMessageId(): Long = lastMessageId.incrementAndGet()

    fun toJson(): JsonObject = JsonObject(
        buildMap {
            put("id", JsonPrimitive(id))
            put("type", JsonPrimitive(type))
            title?.let { put("title", JsonPrimitive(it)) }
            username?.let { put("username", JsonPrimitive(it)) }
            firstName?.let { put("first_name", JsonPrimitive(it)) }
            if (isForum) put("is_forum", JsonPrimitive(true))
        },
    )
}

class Stub(
    val method: String,
    val match: JsonObject,
    val response: ApiResult,
    times: Int,
) {
    private val remaining = AtomicInteger(times)

    fun consume(): Boolean = remaining.getAndUpdate { if (it > 0) it - 1 else it } > 0
}

class Fault(
    val method: String?,
    val error: ApiException,
    times: Int,
    val delay: Duration = Duration.ZERO,
) {
    private val remaining = AtomicInteger(times)

    fun consume(): Boolean = remaining.getAndUpdate { if (it > 0) it - 1 else it } > 0
}

class WebhookState(
    val url: String,
    val secretToken: String?,
    val allowedUpdates: List<String>?,
    val maxConnections: Int,
    val ipAddress: String?,
) {
    @Volatile
    var lastErrorDate: Long? = null

    @Volatile
    var lastErrorMessage: String? = null
}

/** The update queue shared by long polling and webhook delivery. */
class UpdateQueue {
    private val items = ArrayList<JsonObject>()
    private var nextId = 1L
    private val version = MutableStateFlow(0L)
    private val poller = AtomicReference<CompletableDeferred<Unit>?>(null)

    val size get() = synchronized(items) { items.size }

    fun enqueue(build: (Long) -> JsonObject): JsonObject {
        val update = synchronized(items) { build(nextId++).also { items += it } }
        version.value += 1
        return update
    }

    fun snapshot(): List<JsonObject> = synchronized(items) { items.toList() }

    fun remove(updateId: Long) {
        synchronized(items) { items.removeAll { it.long("update_id") == updateId } }
    }

    fun clear() {
        synchronized(items) { items.clear() }
    }

    /** Confirms everything below [offset]; a negative offset keeps only the last `-offset` updates. */
    fun confirm(offset: Long) {
        synchronized(items) {
            if (offset < 0) {
                val keep = (-offset).toInt()
                if (items.size > keep) items.subList(0, items.size - keep).clear()
            } else {
                items.removeAll { (it.long("update_id") ?: 0) < offset }
            }
        }
    }

    suspend fun awaitChange(seen: Long) {
        version.first { it != seen }
    }

    fun currentVersion() = version.value

    /**
     * Long polls like `getUpdates`. A newer poll terminates the older one, which then reports `409 Conflict`.
     */
    suspend fun poll(offset: Long?, limit: Int, timeoutSeconds: Int): List<JsonObject> {
        val mine = CompletableDeferred<Unit>()
        poller.getAndSet(mine)?.complete(Unit)
        try {
            offset?.let(::confirm)
            var result = pending(limit)
            if (result.isEmpty() && timeoutSeconds > 0) {
                if (waitForChange(mine, timeoutSeconds)) throw ApiException.conflict(TERMINATED)
                result = pending(limit)
            }
            return result
        } finally {
            poller.compareAndSet(mine, null)
        }
    }

    /** Returns true when the wait was cut short because a newer poll took over. */
    private suspend fun waitForChange(terminated: CompletableDeferred<Unit>, timeoutSeconds: Int): Boolean =
        coroutineScope {
            val seen = version.value
            val changed = async { awaitChange(seen) }
            val wasTerminated = withTimeoutOrNull(timeoutSeconds * MILLIS) {
                select {
                    terminated.onAwait { true }
                    changed.onAwait { false }
                }
            }
            changed.cancel()
            wasTerminated == true
        }

    private fun pending(limit: Int) = synchronized(items) { items.take(limit) }

    private companion object {
        const val MILLIS = 1000L
        const val TERMINATED =
            "terminated by other getUpdates request; make sure that only one bot instance is running"
    }
}
