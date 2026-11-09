package eu.vendeli.tgbot.types.widget

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object represents an incoming query from a forwarded message with a shared widget created by the bot. Use answerWidgetQuery to answer the query. Each user can send at most 5 queries per 5 seconds to the bot from all its widgets.
 *
 * [Api reference](https://core.telegram.org/bots/api#sharedwidgetquery)
 * @property id Unique identifier for this query
 * @property from Sender
 * @property type Type of the query
 * @property data Data of the query, 1-64 bytes
 * @property widgetId Unique identifier of the shared widget, which sent the query
 */
@Serializable
data class SharedWidgetQuery(
    val id: String,
    val from: User,
    val type: WidgetQueryType,
    val data: String,
    val widgetId: String,
)
