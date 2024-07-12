package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred

/**
 * Provides the ability to do inline request.
 *
 * @param ReturnType
 */
@OptIn(InternalApi::class)
interface InlineActionExt<ReturnType> : Request<ReturnType> {
    /**
     * Make inline request for action.
     *
     * @param inlineMessageId Identifier of the inline message
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun sendInline(inlineMessageId: String, via: TelegramBot) {
        parameters["inline_message_id"] = inlineMessageId.toJsonElement()
        doRequest(via)
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
        return doRequestReturning(via)
    }
}
