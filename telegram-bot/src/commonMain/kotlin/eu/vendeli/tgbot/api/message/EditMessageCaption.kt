@file:Suppress("MatchingDeclarationName", "ktlint:standard:class-signature")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.EditCaptionOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditMessageCaptionAction() :
    Action<Message>(),
    InlineActionExt<Message>,
    BusinessActionExt<Message>,
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    CaptionFeature<EditMessageCaptionAction> {
    @TgAPI.Name("editMessageCaption")
    override val method = "editMessageCaption"
    override val returnType = getReturnType()
    override val options = EditCaptionOptions()
    override val entitiesFieldName: String = "caption_entities"

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagecaption)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param caption New caption of the message, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True, if the caption must be shown above the message media. Supported only for animation, photo and video messages.
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 * @returns [Message]|[Boolean]
 */
@TgAPI
inline fun editMessageCaption(messageId: Long) = EditMessageCaptionAction(messageId)

@TgAPI
inline fun editMessageCaption() = EditMessageCaptionAction()

@TgAPI
fun editCaption() = editMessageCaption()

@TgAPI
fun editCaption(messageId: Long) = editMessageCaption(messageId)
