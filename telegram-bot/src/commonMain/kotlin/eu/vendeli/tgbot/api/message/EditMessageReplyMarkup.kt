@file:Suppress("MatchingDeclarationName", "TooManyFunctions", "ktlint:standard:class-signature")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class EditMessageReplyMarkupAction() :
    Action<Message>(),
    InlineActionExt<Message>,
    BusinessActionExt<Message>,
    MarkupFeature<EditMessageReplyMarkupAction> {
    @TgAPI.Name("editMessageReplyMarkup")
    override val method = "editMessageReplyMarkup"
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagereplymarkup)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 * @returns [Message]|[Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMessageReplyMarkup() = editMarkup()

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMarkup(messageId: Long) = EditMessageReplyMarkupAction(messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMarkup() = EditMessageReplyMarkupAction()

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMessageReplyMarkup(messageId: Long) = editMarkup(messageId)
