package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred

/**
 * Provides the ability to do business request.
 *
 * @param ReturnType
 */

interface BusinessActionExt<ReturnType> : Request<ReturnType> {
    /**
     * Make business request for action.
     *
     * @param businessConnectionId Identifier of the inline message
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun sendBusiness(chatId: Long, businessConnectionId: String, via: TelegramBot) {
        parameters["chat_id"] = chatId.toJsonElement()
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        doRequest(via)
    }

    /**
     * Make a request for action returning its [Response].
     *
     * @param businessConnectionId Identifier of the inline message
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun sendBusinessReturning(
        chatId: Long,
        businessConnectionId: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = chatId.toJsonElement()
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        return doRequestReturning(via)
    }

    @Deprecated(
        "Use sendBusinessReturning instead",
        ReplaceWith("sendBusinessReturning(chatId, businessConnectionId, via)"),
    )
    suspend fun sendBusinessAsync(
        chatId: Long,
        businessConnectionId: String,
        via: TelegramBot,
    ) = sendBusinessReturning(chatId, businessConnectionId, via)
}
