package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.features.Feature
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred

/**
 * Simple action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
interface SimpleAction<ReturnType> : TgAction, Feature {
    /**
     * Send request for action.
     *
     * @param to recipient.
     */
    suspend fun send(to: TelegramBot) {
        to.makeSilentRequest(method, parameters)
    }
}

/**
 * Send async request for action.
 *
 * @param ReturnType response type.
 * @param to recipient.
 */
suspend inline fun <reified ReturnType> SimpleAction<ReturnType>.sendAsync(to: TelegramBot): Deferred<Response<ReturnType>> =
    to.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
