package eu.vendeli.tgbot.mock

import kotlinx.coroutines.Job
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

/** Everything the mock remembers about one bot token. */
class BotState(
    val token: String,
    private val config: MockServerConfig,
) {
    val id: Long = token.substringBefore(':').toLong()
    val username: String = "mock_${id}_bot"

    val chats = ConcurrentHashMap<Long, ChatState>()
    val users = ConcurrentHashMap<Long, JsonObject>()
    val blockedBy = ConcurrentHashMap.newKeySet<Long>()
    val updates = UpdateQueue()
    val files = ConcurrentHashMap<String, StoredFile>()
    val calls = CopyOnWriteArrayList<RecordedCall>()
    val stubs = CopyOnWriteArrayList<Stub>()
    val faults = CopyOnWriteArrayList<Fault>()
    val commands = ConcurrentHashMap<String, JsonElement>()
    val callbackQueries = ConcurrentHashMap<String, Boolean>()
    val limiter = config.rateLimit?.let { RateLimiter(it, config.clock) }
    val fileCounter = AtomicLong()
    val extra = ExtraState()
    private val mediaGroupIds = AtomicLong()
    private val callbackIds = AtomicLong()
    private val pollIdCounter = AtomicLong()

    @Volatile
    var webhook: WebhookState? = null

    @Volatile
    var webhookJob: Job? = null

    @Volatile
    var pollAllowedUpdates: List<String>? = null

    val user: JsonObject by lazy {
        JsonObject(
            mapOf(
                "id" to JsonPrimitive(id),
                "is_bot" to JsonPrimitive(true),
                "first_name" to JsonPrimitive("Mock Bot"),
                "username" to JsonPrimitive(username),
            ),
        )
    }

    fun nextMediaGroupId() = mediaGroupIds.incrementAndGet().toString()

    fun nextCallbackId() = "mock-cb-${callbackIds.incrementAndGet()}"

    fun nextPollId() = "mock-poll-${pollIdCounter.incrementAndGet()}"

    fun registerUser(user: JsonObject) {
        users[user.long("id")!!] = user
    }

    /** Finds a chat by numeric id or `@username`, creating private chats for unknown ids per the config. */
    fun resolveChat(chatId: JsonElement?): ChatState {
        val raw = (chatId as? JsonPrimitive)?.content ?: throw ApiException.badRequest("chat_id is empty")
        raw.toLongOrNull()?.let { numeric ->
            chats[numeric]?.let { return it }
            if (config.unknownChats == UnknownChatPolicy.REJECT) throw ApiException.badRequest("chat not found")
            return chats.computeIfAbsent(numeric) { ChatState(it, inferType(it), firstName = "Mock") }
        }
        val name = raw.removePrefix("@")
        return chats.values.firstOrNull { it.username.equals(name, ignoreCase = true) }
            ?: throw ApiException.badRequest("chat not found")
    }

    fun requireDeliverable(chat: ChatState) {
        if (chat.isPrivate && chat.id in blockedBy) throw ApiException.forbidden("bot was blocked by the user")
        if (chat.isPrivate) return
        // Same wording as the real Bot API server: it names the chat kind and tells kicked from left.
        when (chat.members[id]) {
            "kicked" -> throw ApiException.forbidden("bot was kicked from the ${chat.type} chat")
            "left" -> throw ApiException.forbidden("bot is not a member of the ${chat.type} chat")
        }
    }

    fun reset() {
        chats.clear()
        users.clear()
        blockedBy.clear()
        updates.clear()
        files.clear()
        calls.clear()
        stubs.clear()
        faults.clear()
        commands.clear()
        callbackQueries.clear()
        extra.clear()
        webhookJob?.cancel()
        webhookJob = null
        webhook = null
        pollAllowedUpdates = null
    }

    private fun inferType(id: Long) = when {
        id > 0 -> "private"
        id < SUPERGROUP_THRESHOLD -> "supergroup"
        else -> "group"
    }

    private companion object {
        const val SUPERGROUP_THRESHOLD = -1_000_000_000_000L
    }
}

/** Sliding-window flood control modelled after the limits in the Bot API FAQ. */
class RateLimiter(
    private val limits: RateLimitConfig,
    private val clock: Clock,
) {
    private val perChat = ConcurrentHashMap<Long, ArrayDeque<Long>>()
    private val global = ArrayDeque<Long>()

    fun check(chat: ChatState) {
        val now = clock.millis()
        synchronized(this) {
            val chatWindow = perChat.getOrPut(chat.id) { ArrayDeque() }
            val (limit, window) = if (chat.isPrivate) {
                limits.perChatPerSecond to SECOND
            } else {
                limits.perGroupPerMinute to MINUTE
            }
            retryAfter(chatWindow, limit, window, now)?.let { throw ApiException.tooManyRequests(it) }
            retryAfter(global, limits.globalPerSecond, SECOND, now)?.let { throw ApiException.tooManyRequests(it) }
            chatWindow.addLast(now)
            global.addLast(now)
        }
    }

    private fun retryAfter(window: ArrayDeque<Long>, limit: Int, windowMs: Long, now: Long): Int? {
        while (window.isNotEmpty() && now - window.first() >= windowMs) window.removeFirst()
        if (window.size < limit) return null
        val waitMs = windowMs - (now - window.first())
        return ((waitMs + SECOND - 1) / SECOND).toInt().coerceAtLeast(1)
    }

    private companion object {
        const val SECOND = 1000L
        const val MINUTE = 60_000L
    }
}
