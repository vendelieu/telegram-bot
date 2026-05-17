package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.session.Session
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.types.component.getOrNull
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.Direction
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */

abstract class Action<ReturnType> : TgAction<ReturnType>() {
    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param session Optional session into which any returned [Message] (or list of messages)
     *   will be tracked as [Direction.Outgoing]. Pass `null` (default) for the historical
     *   fire-and-forget behaviour with zero tracking overhead.
     */
    suspend fun send(to: String, via: TelegramBot, session: Session? = null) {
        parameters["chat_id"] = to.toJsonElement()
        if (session == null) doRequest(via) else sendTracked(via, session)
    }

    suspend fun send(to: Long, via: TelegramBot, session: Session? = null) {
        parameters["chat_id"] = to.toJsonElement()
        if (session == null) doRequest(via) else sendTracked(via, session)
    }

    suspend inline fun send(to: User, via: TelegramBot, session: Session? = null): Unit =
        send(to.id, via, session)

    suspend inline fun send(to: Chat, via: TelegramBot, session: Session? = null): Unit =
        send(to.id, via, session)

    /**
     * Make a request for action returning its [Response].
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param session Optional session. When non-null any returned [Message] / [List] of messages
     *   is tracked as [Direction.Outgoing] before the caller receives the response; the returned
     *   [Deferred] still completes with the original [Response].
     */
    suspend fun sendReturning(
        to: String,
        via: TelegramBot,
        session: Session? = null,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return sendReturningTracked(via, session)
    }

    suspend fun sendReturning(
        to: Long,
        via: TelegramBot,
        session: Session? = null,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return sendReturningTracked(via, session)
    }

    suspend inline fun sendReturning(
        to: User,
        via: TelegramBot,
        session: Session? = null,
    ): Deferred<Response<out ReturnType>> = sendReturning(to.id, via, session)

    suspend inline fun sendReturning(
        to: Chat,
        via: TelegramBot,
        session: Session? = null,
    ): Deferred<Response<out ReturnType>> = sendReturning(to.id, via, session)

    @PublishedApi
    internal suspend fun sendTracked(via: TelegramBot, session: Session) {
        val response = doRequestReturning(via).await()
        val result = response.getOrNull() ?: return
        session.recordOutgoing(result)
    }

    @PublishedApi
    internal suspend fun sendReturningTracked(
        via: TelegramBot,
        session: Session?,
    ): Deferred<Response<out ReturnType>> {
        val deferred = doRequestReturning(via)
        if (session == null) return deferred
        val response = deferred.await()
        response.getOrNull()?.let { session.recordOutgoing(it) }
        return CompletableDeferred(response)
    }
}

private suspend fun Session.recordOutgoing(result: Any?) {
    when (result) {
        is Message -> track(result, Direction.Outgoing)
        is List<*> -> result.filterIsInstance<Message>().forEach { track(it, Direction.Outgoing) }
    }
}
