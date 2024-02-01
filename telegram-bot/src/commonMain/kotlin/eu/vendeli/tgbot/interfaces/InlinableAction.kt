package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred

/**
 * Provides the ability to do inline request.
 *
 * @param ReturnType
 */
abstract class InlinableAction<ReturnType> : Action<ReturnType>() {
    /**
     * Make inline request for action.
     *
     * @param inlineMessageId Identifier of the inline message
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun sendInline(inlineMessageId: String, via: TelegramBot) {
        parameters["inline_message_id"] = inlineMessageId.toJsonElement()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    /**
     * Make a request for action returning its [Response].
     *
     * @param inlineMessageId Identifier of the inline message
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun sendInlineAsync(
        inlineMessageId: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["inline_message_id"] = inlineMessageId.toJsonElement()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }
}
