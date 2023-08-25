package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import kotlinx.coroutines.Deferred
import kotlin.collections.set

/**
 * Media action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
interface MediaAction<ReturnType> : Action<ReturnType>, TgAction<ReturnType> {
    /**
     * Is this action contain InputFile in payload.
     */
    val MediaAction<ReturnType>.inputFilePresence: Boolean

    /**
     * Field name used as target id.
     */
    val MediaAction<ReturnType>.idRefField: String get() = "chat_id"

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: String, via: TelegramBot) {
        parameters[idRefField] = to
        via.makeSilentRequest(method, parameters, inputFilePresence)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: Long, via: TelegramBot) {
        parameters[idRefField] = to
        via.makeSilentRequest(method, parameters, inputFilePresence)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: User, via: TelegramBot) {
        parameters[idRefField] = to.id
        via.makeSilentRequest(method, parameters, inputFilePresence)
    }

    /**
     * Make request with ability operating over response.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun sendAsync(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters[idRefField] = to
        return via.makeRequestAsync(method, parameters, returnType, wrappedDataType, inputFilePresence)
    }

    /**
     * Make request with ability operating over response.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun sendAsync(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters[idRefField] = to
        return via.makeRequestAsync(method, parameters, returnType, wrappedDataType, inputFilePresence)
    }

    /**
     * Make request with ability operating over response.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun sendAsync(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters[idRefField] = to.id
        return via.makeRequestAsync(method, parameters, returnType, wrappedDataType, inputFilePresence)
    }
}
