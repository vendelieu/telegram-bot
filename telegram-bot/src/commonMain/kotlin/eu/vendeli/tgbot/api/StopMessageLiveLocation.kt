@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.BusinessActionExt
import eu.vendeli.tgbot.interfaces.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@Suppress("ktlint:standard:class-signature")
class StopMessageLiveLocationAction() :
    Action<Message>(),
    InlineActionExt<Message>,
    BusinessActionExt<Message>,
    MarkupFeature<StopMessageLiveLocationAction> {
    override val method = TgMethod("stopMessageLiveLocation")
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to stop updating a live location message before live_period expires. On success, if the message is not an inline message, the edited Message is returned, otherwise True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#stopmessagelivelocation)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message with live location to stop
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 * @returns [Message]|[Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun stopMessageLiveLocation(messageId: Long) = StopMessageLiveLocationAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun stopMessageLiveLocation() = StopMessageLiveLocationAction()
