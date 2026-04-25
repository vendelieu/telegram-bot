package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.interfaces.session.SessionManager
import eu.vendeli.tgbot.types.component.MessageReference
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.types.session.SessionKey

/**
 * Pipeline interceptor that records incoming message-bearing updates into every live session
 * whose subscription predicate matches.
 *
 * Self-gating: when no sessions are open ([SessionManager.isIdle]) the interceptor short-circuits
 * after a single map check, so bots that never touch sessions pay effectively zero per-update cost.
 * Registered unconditionally at [eu.vendeli.tgbot.core.ProcessingPipePhase.Setup].
 */
internal class SessionTrackingInterceptor(
    private val sessions: SessionManager,
) : PipelineInterceptor {
    @Suppress("detekt:ReturnCount")
    override suspend fun invoke(context: ProcessingContext) {
        if (sessions.isIdle()) return
        val update = context.update
        if (update !is MessageReference) return
        val matched = sessions.matchingSubscriptions(update)
        if (matched.isEmpty()) return
        for (key in matched) {
            sessions.sessionFor(key).track(update, Direction.Incoming)
        }
    }
}

private fun SessionManager.sessionFor(key: SessionKey) = when (key) {
    is SessionKey.ChatUser -> get(key.chatId, key.userId, key.qualifier)
    is SessionKey.Chat -> get(key.chatId, qualifier = key.qualifier)
}
