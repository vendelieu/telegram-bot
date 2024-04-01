package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonUnquotedLiteral
import kotlinx.serialization.json.jsonPrimitive
import kotlin.collections.set

/**
 * Media action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
abstract class MediaAction<ReturnType> : Action<ReturnType>() {
    /**
     * Field name used as target id.
     */
    internal open val idRefField: String = "chat_id"

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: String, via: TelegramBot) {
        parameters[idRefField] = to.toJsonElement()
        handleParseModeInMultipart()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    override suspend fun send(to: Long, via: TelegramBot) {
        parameters[idRefField] = to.toJsonElement()
        handleParseModeInMultipart()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    override suspend fun send(to: User, via: TelegramBot) {
        parameters[idRefField] = to.id.toJsonElement()
        handleParseModeInMultipart()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    override suspend fun send(to: Chat, via: TelegramBot) {
        parameters[idRefField] = to.id.toJsonElement()
        handleParseModeInMultipart()
        via.makeSilentRequest(method, parameters, multipartData)
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
        parameters[idRefField] = to.toJsonElement()
        handleParseModeInMultipart()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    override suspend fun sendAsync(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters[idRefField] = to.toJsonElement()
        handleParseModeInMultipart()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    override suspend fun sendAsync(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters[idRefField] = to.id.toJsonElement()
        handleParseModeInMultipart()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    override suspend fun sendAsync(
        to: Chat,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters[idRefField] = to.id.toJsonElement()
        handleParseModeInMultipart()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun handleParseModeInMultipart() {
        if (multipartData.isNotEmpty()) parameters["parse_mode"]?.let {
            parameters["parse_mode"] = JsonUnquotedLiteral(it.jsonPrimitive.content)
        }
    }
}
