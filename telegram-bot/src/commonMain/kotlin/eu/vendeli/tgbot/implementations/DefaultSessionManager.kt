package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.deleteMessages
import eu.vendeli.tgbot.interfaces.helper.LoggerFactory
import eu.vendeli.tgbot.interfaces.session.Session
import eu.vendeli.tgbot.interfaces.session.SessionManager
import eu.vendeli.tgbot.interfaces.session.SessionStorage
import eu.vendeli.tgbot.types.component.MessageReference
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.chatOrNull
import eu.vendeli.tgbot.types.component.detectKind
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.SessionKeyStrategy
import eu.vendeli.tgbot.types.session.TrackedMessage
import eu.vendeli.tgbot.utils.common.fqName
import eu.vendeli.tgbot.utils.common.safeCast
import io.ktor.util.collections.ConcurrentMap
import io.ktor.util.logging.debug
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TELEGRAM_DELETE_BATCH_SIZE = 100

/**
 * Default [SessionManager] — dispatches deletions through the given [bot], stores tracked entries
 * in [storage], and uses [strategy] to derive keys from updates. Public so custom
 * [eu.vendeli.tgbot.interfaces.session.SessionManagerFactory] implementations can delegate to it.
 *
 * Owns the live-subscription registry: every `get`/`of` call auto-subscribes the resolved key
 * with a default predicate derived from the key shape, and `Session.close()` unsubscribes plus
 * clears storage. The pipeline interceptor consults [matchingSubscriptions] / [isIdle] to decide
 * what to track.
 */
class DefaultSessionManager(
    private val bot: TelegramBot,
    private val storage: SessionStorage,
    private val strategy: SessionKeyStrategy,
    loggerFactory: LoggerFactory,
) : SessionManager {
    private val logger = loggerFactory.get(this::class.fqName)
    private val subscriptions = ConcurrentMap<SessionKey, (ProcessedUpdate) -> Boolean>()
    private val sessionCache = ConcurrentMap<SessionKey, Session>()

    override fun get(chatId: Long, userId: Long?, qualifier: String?): Session {
        val key = if (userId != null) {
            SessionKey.ChatUser(chatId, userId, qualifier)
        } else {
            SessionKey.Chat(chatId, qualifier)
        }
        autoSubscribe(key)
        return sessionFor(key)
    }

    override fun of(update: ProcessedUpdate, qualifier: String?): Session? {
        val key = strategy.resolve(update)?.withQualifier(qualifier) ?: return null
        autoSubscribe(key)
        return sessionFor(key)
    }

    override fun subscribe(key: SessionKey, predicate: (ProcessedUpdate) -> Boolean) {
        subscriptions[key] = predicate
    }

    /**
     * Idempotent default-predicate registration used by [get] / [of]. Preserves any predicate
     * a caller already installed via the public [subscribe].
     */
    private fun autoSubscribe(key: SessionKey) {
        subscriptions.computeIfAbsent(key) { defaultPredicateFor(key) }
    }

    override fun unsubscribe(key: SessionKey) {
        subscriptions.remove(key)
        sessionCache.remove(key)
    }

    private fun sessionFor(key: SessionKey): Session =
        sessionCache.computeIfAbsent(key) { DefaultSession(key, bot, storage, this, logger) }

    override fun matchingSubscriptions(update: ProcessedUpdate): List<SessionKey> {
        if (subscriptions.isEmpty()) return emptyList()
        return subscriptions.entries.mapNotNull { (key, pred) ->
            key.takeIf { runCatching { pred(update) }.getOrDefault(false) }
        }
    }

    override fun isIdle(): Boolean = subscriptions.isEmpty()

    private fun defaultPredicateFor(key: SessionKey): (ProcessedUpdate) -> Boolean = when (key) {
        is SessionKey.ChatUser -> { update ->
            update.chatOrNull?.id == key.chatId && update.userOrNull?.id == key.userId
        }

        is SessionKey.Chat -> { update -> update.chatOrNull?.id == key.chatId }
    }
}

@OptIn(ExperimentalTime::class)
private class DefaultSession(
    override val key: SessionKey,
    override val bot: TelegramBot,
    private val storage: SessionStorage,
    private val manager: SessionManager,
    private val logger: io.ktor.util.logging.Logger,
) : Session {
    override val chatId: Long get() = key.chatId
    override val userId: Long? get() = key.safeCast<SessionKey.ChatUser>()?.userId

    override suspend fun track(message: Message, direction: Direction) {
        storage.add(
            key,
            TrackedMessage(
                messageId = message.messageId,
                chatId = message.chat.id,
                userId = message.from?.id,
                kind = message.detectKind(),
                direction = direction,
                businessConnectionId = message.businessConnectionId,
                at = Clock.System.now(),
            ),
        )
    }

    override suspend fun track(update: ProcessedUpdate, direction: Direction) {
        val ref = update.safeCast<MessageReference>() ?: return
        val message = with(ref) { getMessage() } ?: return
        storage.add(
            key,
            TrackedMessage(
                messageId = message.messageId,
                chatId = message.chat.id,
                userId = update.userOrNull?.id ?: message.from?.id,
                kind = message.detectKind(),
                direction = direction,
                businessConnectionId = message.businessConnectionId,
                at = Clock.System.now(),
            ),
        )
    }

    override suspend fun messages(): List<TrackedMessage> = storage.list(key)

    override suspend fun clear(bot: TelegramBot, predicate: (TrackedMessage) -> Boolean): Int {
        val matched = storage.list(key).filter(predicate)
        if (matched.isEmpty()) return 0

        matched
            .groupBy { it.chatId }
            .forEach { (chatId, entries) ->
                entries.chunked(TELEGRAM_DELETE_BATCH_SIZE).forEach { chunk ->
                    val ids = chunk.map { it.messageId }
                    runCatching { deleteMessages(ids).sendReturning(chatId, bot).await() }
                        .onSuccess { response ->
                            if (!response.ok) {
                                logger.warn("session($key) delete batch for chat=$chatId size=${chunk.size} failed")
                            } else {
                                logger.debug { "session($key) delete batch for chat=$chatId size=${chunk.size}" }
                            }
                        }.onFailure {
                            logger.error("session($key) delete batch for chat=$chatId threw", it)
                        }
                }
            }

        return storage.remove(key, predicate)
    }

    override suspend fun forget(predicate: (TrackedMessage) -> Boolean): Int =
        storage.remove(key, predicate)

    override suspend fun close() {
        manager.unsubscribe(key)
        storage.clear(key)
    }
}
