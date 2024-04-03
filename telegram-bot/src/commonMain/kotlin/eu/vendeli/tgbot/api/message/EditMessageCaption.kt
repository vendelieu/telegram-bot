package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditCaptionOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditMessageCaptionAction() :
    Action<Message>(),
    InlineActionExt<Message>,
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    CaptionFeature<EditMessageCaptionAction> {
    override val method = TgMethod("editMessageCaption")
    override val returnType = getReturnType()
    override val options = EditCaptionOptions()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
 * Api reference: https://core.telegram.org/bots/api#editmessagecaption
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param caption New caption of the message, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 * @returns [Message]|[Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun editMessageCaption(messageId: Long) = EditMessageCaptionAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageCaption() = EditMessageCaptionAction()

fun editCaption() = editMessageCaption()
fun editCaption(messageId: Long) = editMessageCaption(messageId)
