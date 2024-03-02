@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditMessageReplyMarkupAction() :
    InlinableAction<Message>(),
    MarkupFeature<EditMessageReplyMarkupAction> {
    override val method = TgMethod("editMessageReplyMarkup")
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
 * Api reference: https://core.telegram.org/bots/api#editmessagereplymarkup
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 * @returns [Message]|[Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun editMessageReplyMarkup() = editMarkup()

@Suppress("NOTHING_TO_INLINE")
inline fun editMarkup(messageId: Long) = EditMessageReplyMarkupAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMarkup() = EditMessageReplyMarkupAction()

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageReplyMarkup(messageId: Long) = editMarkup(messageId)
