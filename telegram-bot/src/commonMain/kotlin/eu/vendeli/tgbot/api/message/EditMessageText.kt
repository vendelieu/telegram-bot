package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.EditMessageOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class EditMessageTextAction private constructor() :
    Action<Message>(),
    InlineActionExt<Message>,
    BusinessActionExt<Message>,
    EntitiesCtxBuilder<EditMessageTextAction>,
    OptionsFeature<EditMessageTextAction, EditMessageOptions>,
    MarkupFeature<EditMessageTextAction>,
    EntitiesFeature<EditMessageTextAction> {
        @TgAPI.Name("editMessageText")
        override val method = "editMessageText"
        override val returnType = getReturnType()
        override val options = EditMessageOptions()

        constructor(messageId: Long, text: String) : this() {
            parameters["message_id"] = messageId.toJsonElement()
            parameters["text"] = text.toJsonElement()
        }

        constructor(text: String) : this() {
            parameters["text"] = text.toJsonElement()
        }

        internal constructor(block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String) : this() {
            parameters["text"] = block(this).toJsonElement()
        }
    }

/**
 * Use this method to edit text and game messages. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
 * Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagetext)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param text New text of the message, 1-4096 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @param linkPreviewOptions Link preview generation options for the message
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 * @returns [Message]|[Boolean]
 */
@TgAPI
inline fun editMessageText(messageId: Long, block: () -> String) = editText(messageId, block)

@TgAPI
fun editMessageText(block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String) = EditMessageTextAction(block)

@TgAPI
inline fun editText(messageId: Long, block: () -> String) = EditMessageTextAction(messageId, text = block())

@TgAPI
fun editText(block: EntitiesCtxBuilder<EditMessageTextAction>.() -> String) = EditMessageTextAction(block)
