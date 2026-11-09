@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.widget

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.options.WidgetOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class SendWidgetAction(
    html: String,
) : MediaAction<Message>(),
    OptionsFeature<SendWidgetAction, WidgetOptions>,
    MarkupFeature<SendWidgetAction>,
    CaptionFeature<SendWidgetAction> {
    @TgAPI.Name("sendWidget")
    override val method = "sendWidget"
    override val returnType = getReturnType()
    override val options = WidgetOptions()

    init {
        parameters["html"] = html.toJsonElement()
    }
}

/**
 * Use this method to send a message with an HTML widget. Currently, in the production environment, widgets can be sent only to private chats, and sent widgets automatically have protected content and therefore can't be forwarded. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendwidget)
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param html HTML code of the widget, 1-32768 bytes. The code must not exceed 4096 bytes when compressed by zlib with the maximum compression level.
 * @param width Width of the widget in logical pixels; omit if the widget must occupy the whole width of the message bubble. The width is clamped by the app to the range from 200 to the maximum width of the message bubble.
 * @param height Height of the widget in logical pixels; omit if the height of the widget isn't fixed. The height is clamped by the app to the range 50-600.
 * @param publicData Public data of the widget, 0-1024 bytes, which will be available wherever the widget is shown. If the widget is shared, then all messages with it will have the same public data.
 * @param chatData Data of the widget, which is specific to the message, 0-1024 bytes
 * @param caption Widget caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the widget caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True if the caption must be shown above the widget
 * @param allowQueries Pass True to allow the widget to send queries to the bot
 * @param isShared Pass True to create a shared widget, which can be sent again using the method sendSharedWidget
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user.
 * @returns [Message]
 */
@TgAPI
inline fun sendWidget(html: String) = SendWidgetAction(html)

@TgAPI
inline fun widget(html: String) = sendWidget(html)
