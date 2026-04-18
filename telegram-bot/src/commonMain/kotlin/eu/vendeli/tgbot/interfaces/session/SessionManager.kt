package eu.vendeli.tgbot.interfaces.session

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Entry point for accessing [Session] instances from handler code.
 *
 * Exposed as `TelegramBot.sessions` once the `sessions { }` configuration block has run.
 *
 * Every accessor takes an optional `qualifier`: pass a non-null value to address a
 * distinct parallel session for the same chat/user (e.g. `"wizard"` and `"support"`
 * running side-by-side). Omit it (or pass `null`) for the default, unqualified session.
 */
interface SessionManager {
    /** Obtain a session by raw ids. */
    fun get(chatId: Long, userId: Long? = null, qualifier: String? = null): Session

    /** Obtain a session by domain objects. */
    fun get(chat: Chat, user: User? = null, qualifier: String? = null): Session =
        get(chat.id, user?.id, qualifier)

    /**
     * Resolve the session appropriate for [update] via the configured key strategy and,
     * optionally, tag it with [qualifier]. Returns `null` when the update carries no
     * chat context (pure inline query, poll, …).
     */
    fun of(update: ProcessedUpdate, qualifier: String? = null): Session?
}
