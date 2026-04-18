package eu.vendeli.tgbot.core.interceptors

import eu.vendeli.tgbot.core.PipelineInterceptor
import eu.vendeli.tgbot.interfaces.session.SessionManager
import eu.vendeli.tgbot.types.component.MessageReference
import eu.vendeli.tgbot.types.component.ProcessingContext
import eu.vendeli.tgbot.types.session.Direction

/**
 * Pipeline interceptor that records every incoming message-bearing update into its session.
 *
 * Registered at [eu.vendeli.tgbot.core.ProcessingPipePhase.Setup] when
 * [eu.vendeli.tgbot.types.configuration.SessionConfiguration.trackIncoming] is `true`.
 * Reads the update straight from [ProcessingContext.update] — no `CoroutineContext` or
 * thread-local lookup is involved, so tracking cannot be lost by inner `launch { }` /
 * `withContext { }` calls.
 */
internal class SessionTrackingInterceptor(
    private val sessions: SessionManager,
) : PipelineInterceptor {
    override suspend fun invoke(context: ProcessingContext) {
        val update = context.update
        if (update !is MessageReference) return
        val session = sessions.of(update) ?: return
        session.track(update, Direction.Incoming)
    }
}
