@file:Suppress("MatchingDeclarationName", "ktlint:standard:class-signature")

package eu.vendeli.tgbot.api.widget

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.EditMessageWidgetDataOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditMessageWidgetDataAction() :
    Action<Boolean>(),
    InlineActionExt<Boolean>,
    OptionsFeature<EditMessageWidgetDataAction, EditMessageWidgetDataOptions> {
    @TgAPI.Name("editMessageWidgetData")
    override val method = "editMessageWidgetData"
    override val returnType = getReturnType()
    override val options = EditMessageWidgetDataOptions()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to edit data of a widget in a message sent by the bot or via the bot (for inline bots). Public data of a shared widget can be changed at most once per second. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagewidgetdata)
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target supergroup in the format @username.
 * @param messageId Required if inline_message_id is not specified. Identifier of the message with the widget.
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message.
 * @param publicData New public data of the widget, 0-1024 bytes; pass an empty string to remove the public data. If omitted, then the public data isn't changed.
 * @param chatData New data of the widget, which is specific to the message, 0-1024 bytes; pass an empty string to remove the data. If omitted, then the data isn't changed.
 * @returns [Boolean]
 */
@TgAPI
inline fun editMessageWidgetData() = EditMessageWidgetDataAction()

@TgAPI
inline fun editMessageWidgetData(messageId: Long) = EditMessageWidgetDataAction(messageId)
