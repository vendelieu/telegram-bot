package eu.vendeli.tgbot.types.session

import kotlinx.serialization.Serializable

/**
 * Identifier of a tracked session.
 *
 * Sealed so backends can pattern-match (and eventually persist) without touching arbitrary strings.
 * Every variant exposes at least [chatId]; per-user shapes carry the acting user's id alongside it.
 *
 * An optional [qualifier] distinguishes multiple concurrent sessions that share the same
 * chat (and user) — useful when a single chat hosts several independent flows at once
 * (e.g. a wizard and a support ticket running in parallel).
 */
@Serializable
sealed class SessionKey {
    abstract val chatId: Long
    abstract val qualifier: String?

    /** Chat-wide session shared by every participant of [chatId]. */
    @Serializable
    data class Chat(
        override val chatId: Long,
        override val qualifier: String? = null,
    ) : SessionKey()

    /** Per-user session scoped to a specific [userId] within [chatId]. */
    @Serializable
    data class ChatUser(
        override val chatId: Long,
        val userId: Long,
        override val qualifier: String? = null,
    ) : SessionKey()

    /** Return a copy of this key tagged with [qualifier]. */
    fun withQualifier(qualifier: String?): SessionKey = when (this) {
        is Chat -> copy(qualifier = qualifier)
        is ChatUser -> copy(qualifier = qualifier)
    }
}
