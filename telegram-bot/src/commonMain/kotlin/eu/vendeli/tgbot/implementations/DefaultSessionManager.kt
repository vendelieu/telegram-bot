package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.deleteMessages
import eu.vendeli.tgbot.interfaces.helper.LoggerFactory
import eu.vendeli.tgbot.interfaces.session.Session
import eu.vendeli.tgbot.interfaces.session.SessionManager
import eu.vendeli.tgbot.interfaces.session.SessionStorage
import eu.vendeli.tgbot.types.component.MessageReference
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.component.userOrNull
import eu.vendeli.tgbot.types.component.detectKind
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.SessionKeyStrategy
import eu.vendeli.tgbot.types.session.TrackedMessage
import eu.vendeli.tgbot.utils.common.fqName
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TELEGRAM_DELETE_BATCH_SIZE = 100

/**
 * Default [SessionManager] — dispatches deletions through the given [bot], stores tracked entries
 * in [storage], and uses [strategy] to derive keys from updates. Public so custom
 * [eu.vendeli.tgbot.interfaces.session.SessionManagerFactory] implementations can delegate to it.
 */
class DefaultSessionManager(
    private val bot: TelegramBot,
    private val storage: SessionStorage,
    private val strategy: SessionKeyStrategy,
    loggerFactory: LoggerFactory,
) : SessionManager {
    private val logger = loggerFactory.get(this::class.fqName)

    override fun get(chatId: Long, userId: Long?, qualifier: String?): Session {
        val key = if (userId != null) {
            SessionKey.ChatUser(chatId, userId, qualifier)
        } else {
            SessionKey.Chat(chatId, qualifier)
        }
        return DefaultSession(key, bot, storage, logger)
    }

    override fun of(update: ProcessedUpdate, qualifier: String?): Session? {
        val key = strategy.resolve(update)?.withQualifier(qualifier) ?: return null
        return DefaultSession(key, bot, storage, logger)
    }
}

@OptIn(ExperimentalTime::class)
private class DefaultSession(
    override val key: SessionKey,
    override val bot: TelegramBot,
    private val storage: SessionStorage,
    private val logger: io.ktor.util.logging.Logger,
) : Session {
    override val chatId: Long get() = key.chatId
    override val userId: Long? get() = (key as? SessionKey.ChatUser)?.userId

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
        val ref = update as? MessageReference ?: return
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
                            response.ok.takeIf { !it }?.let {
                                logger.warn(
                                    "session($key) delete batch for chat=$chatId size=${chunk.size} failed",
                                )
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
}
