package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.session.Session
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Response
import kotlinx.coroutines.Deferred

// -----------------------------------------------------------------------------
// Context-parameter overloads that pull [Session] from the enclosing scope.
//
// Idiomatic usage is `with(session) { sendMessage("...").send(bot) }`: inside that
// block every send inherits the session without threading it through each call.
// -----------------------------------------------------------------------------

context(session: Session)
suspend fun <T> Action<T>.send(to: Long, via: TelegramBot) = send(to, via, session)

context(session: Session)
suspend fun <T> Action<T>.send(to: String, via: TelegramBot) = send(to, via, session)

context(session: Session)
suspend inline fun <T> Action<T>.send(to: User, via: TelegramBot) = send(to.id, via, session)

context(session: Session)
suspend inline fun <T> Action<T>.send(to: Chat, via: TelegramBot) = send(to.id, via, session)

/** Zero-argument send that targets the session's own [Session.chatId]. */
context(session: Session)
suspend fun <T> Action<T>.send(via: TelegramBot) = send(session.chatId, via, session)

context(session: Session)
suspend fun <T> Action<T>.sendReturning(to: Long, via: TelegramBot): Deferred<Response<out T>> =
    sendReturning(to, via, session)

context(session: Session)
suspend fun <T> Action<T>.sendReturning(to: String, via: TelegramBot): Deferred<Response<out T>> =
    sendReturning(to, via, session)

context(session: Session)
suspend inline fun <T> Action<T>.sendReturning(to: User, via: TelegramBot): Deferred<Response<out T>> =
    sendReturning(to.id, via, session)

context(session: Session)
suspend inline fun <T> Action<T>.sendReturning(to: Chat, via: TelegramBot): Deferred<Response<out T>> =
    sendReturning(to.id, via, session)

/** Zero-argument sendReturning that targets the session's own [Session.chatId]. */
context(session: Session)
suspend fun <T> Action<T>.sendReturning(via: TelegramBot): Deferred<Response<out T>> =
    sendReturning(session.chatId, via, session)
