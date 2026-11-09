package eu.vendeli.tgbot.types.widget

import kotlinx.serialization.Serializable

/**
 * This object describes an HTML widget.
 *
 * [Api reference](https://core.telegram.org/bots/api#widget)
 * @property id Optional. Unique identifier of the shared widget, which can be used to send the widget again using the method sendSharedWidget; omitted if the widget isn't shared
 * @property width Optional. Width of the widget in logical pixels; omitted if the widget must occupy the whole width of the message bubble
 * @property height Optional. Height of the widget in logical pixels; omitted if the height of the widget isn't fixed
 * @property html HTML code of the widget
 * @property publicData Optional. Public data of the widget, which is available wherever the widget is shown. All messages with a shared widget have the same public data.
 * @property chatData Optional. Data of the widget, which is specific to the message
 */
@Serializable
data class Widget(
    val html: String,
    val id: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val publicData: String? = null,
    val chatData: String? = null,
)
