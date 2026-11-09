package eu.vendeli.tgbot.types.widget

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * This object represents an incoming query from a widget in a message sent by the bot or via the bot. Use answerWidgetQuery to answer the query. Each user can send at most 5 queries per 5 seconds to the bot from all its widgets.
 *
 * [Api reference](https://core.telegram.org/bots/api#widgetquery)
 * @property id Unique identifier for this query
 * @property from Sender
 * @property chat Optional. Chat with the message containing the widget; omitted if the widget is in an inline message
 * @property messageId Optional. Identifier of the message containing the widget; omitted if the widget is in an inline message
 * @property inlineMessageId Optional. Identifier of the inline message containing the widget
 * @property chatInstance Global identifier, uniquely corresponding to the chat to which the message with the widget was sent
 * @property chatType Optional. For widgets in inline messages, type of the chat to which the message was sent. Can be either "sender" for a private chat with the inline query sender, "private", "group", "supergroup", or "channel".
 * @property type Type of the query
 * @property data Data of the query, 1-64 bytes
 * @property widgetId Optional. Unique identifier of the shared widget, which sent the query; omitted if the widget isn't shared
 */
@Serializable
data class WidgetQuery(
    val id: String,
    val from: User,
    val chatInstance: String,
    val type: WidgetQueryType,
    val data: String,
    val chat: Chat? = null,
    val messageId: Int? = null,
    val inlineMessageId: String? = null,
    val chatType: String? = null,
    val widgetId: String? = null,
)
