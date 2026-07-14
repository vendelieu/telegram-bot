@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SendChatJoinRequestWebAppAction(
    chatJoinRequestQueryId: String,
    webAppUrl: String,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("sendChatJoinRequestWebApp")
    override val method = "sendChatJoinRequestWebApp"
    override val returnType = getReturnType()

    init {
        parameters["chat_join_request_query_id"] = chatJoinRequestQueryId.toJsonElement()
        parameters["web_app_url"] = webAppUrl.toJsonElement()
    }
}

/**
 * Use this method to process a received chat join request query by showing a Mini App to the user before deciding the outcome. Call answerChatJoinRequestQuery to resolve the join request query based on the user interaction with the Mini App. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendchatjoinrequestwebapp)
 * @param chatJoinRequestQueryId Unique identifier of the join request query
 * @param webAppUrl An HTTPS URL of a Web App to be opened with additional data as specified in Initializing Web Apps
 * @returns [Boolean]
 */
@TgAPI
inline fun sendChatJoinRequestWebApp(chatJoinRequestQueryId: String, webAppUrl: String) =
    SendChatJoinRequestWebAppAction(chatJoinRequestQueryId, webAppUrl)
