package eu.vendeli.tgbot.interfaces.session

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.TrackedMessage

/**
 * Handle on a single logical conversation slice.
 *
 * A session remembers every message that flows through it (both directions) so it can later
 * wipe them from Telegram in bulk — optionally filtered by an arbitrary predicate.
 *
 * Sessions never rely on `CoroutineContext`/thread-local state: callers pass the instance
 * explicitly (via DI, `with(session) { … }`, or an extra `send` parameter), so tracking
 * can never be lost when handlers launch child coroutines.
 */
interface Session {
    /** Composite identifier under which this session's entries are stored. */
    val key: SessionKey

    /** Target chat for zero-argument sends inside a `with(session) { … }` scope. */
    val chatId: Long

    /** Acting user, when the session is per-user. */
    val userId: Long?

    /** Owning bot; captured so extensions can send without threading `bot` through every call. */
    val bot: TelegramBot

    /** Record a message that was sent to or received from Telegram. */
    suspend fun track(message: Message, direction: Direction = Direction.Outgoing)

    /** Record the message carried by [update] (requires the update to expose a [Message]). */
    suspend fun track(update: ProcessedUpdate, direction: Direction = Direction.Incoming)

    /** Snapshot of every entry currently held. */
    suspend fun messages(): List<TrackedMessage>

    /**
     * Delete matching messages from Telegram and drop them from storage.
     *
     * Entries are grouped by [TrackedMessage.chatId] and dispatched in batches of 100
     * (Telegram's `deleteMessages` limit). Storage is cleared regardless of per-batch outcome,
     * so transient API errors don't leak entries forever.
     *
     * @return number of entries removed from storage.
     */
    suspend fun clear(
        bot: TelegramBot = this.bot,
        predicate: (TrackedMessage) -> Boolean = { true },
    ): Int

    /** Drop matching entries from storage without touching Telegram. */
    suspend fun forget(predicate: (TrackedMessage) -> Boolean = { true }): Int

    /**
     * End this session: unsubscribe from incoming updates and drop every stored entry.
     * After [close] further updates won't be auto-tracked under [key]. The session object
     * itself remains usable — calling `bot.sessions.get(...)` (or [track]) again resubscribes.
     */
    suspend fun close()
}
