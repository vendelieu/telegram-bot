package eu.vendeli.tgbot.interfaces.session

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.types.session.SessionKey

/**
 * Entry point for accessing [Session] instances from handler code.
 *
 * Exposed as `TelegramBot.sessions`. The subsystem is always-on with sensible defaults — the
 * `sessions { }` configuration block is only required to customize storage, key strategy, or
 * the manager factory.
 *
 * Tracking of incoming messages is predicate-based: every accessor implicitly subscribes the
 * resolved [SessionKey] to incoming updates that target the same chat (and user, when scoped
 * per-user). The pipeline interceptor only writes to storage for keys with a live subscription,
 * so bots that never touch sessions pay no storage cost.
 *
 * Every accessor takes an optional `qualifier`: pass a non-null value to address a distinct
 * parallel session for the same chat/user (e.g. `"wizard"` and `"support"` running side-by-side).
 */
interface SessionManager {
    /** Obtain a session by raw ids; auto-subscribes the resolved key. */
    fun get(chatId: Long, userId: Long? = null, qualifier: String? = null): Session

    /** Obtain a session by domain objects; auto-subscribes the resolved key. */
    fun get(chat: Chat, user: User? = null, qualifier: String? = null): Session =
        get(chat.id, user?.id, qualifier)

    /**
     * Resolve the session appropriate for [update] via the configured key strategy and,
     * optionally, tag it with [qualifier]; auto-subscribes the resolved key. Returns `null`
     * when the update carries no chat context (pure inline query, poll, …).
     */
    fun of(update: ProcessedUpdate, qualifier: String? = null): Session?

    /**
     * Register [predicate] so the tracking interceptor records updates matching it under [key].
     * Repeated calls with the same [key] overwrite the prior predicate (idempotent for default
     * predicates — intentional for callers refining the match).
     */
    fun subscribe(key: SessionKey, predicate: (ProcessedUpdate) -> Boolean)

    /** Drop the subscription for [key], if any. */
    fun unsubscribe(key: SessionKey)

    /** Keys whose predicate currently matches [update]. */
    fun matchingSubscriptions(update: ProcessedUpdate): List<SessionKey>

    /** True when no subscriptions are registered — interceptor short-circuits on this. */
    fun isIdle(): Boolean
}
