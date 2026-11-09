@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.widget

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class AnswerWidgetQueryAction(
    widgetQueryId: String,
    data: String? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("answerWidgetQuery")
    override val method = "answerWidgetQuery"
    override val returnType = getReturnType()

    init {
        parameters["widget_query_id"] = widgetQueryId.toJsonElement()
        data?.let { parameters["data"] = it.toJsonElement() }
    }
}

/**
 * Use this method to send an answer to a query from a widget. The query must be answered within 15 seconds. The answer is received only by the widget that sent the query and doesn't change data of the widget; use editMessageWidgetData or editSharedWidgetData to change it. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#answerwidgetquery)
 * @param widgetQueryId Unique identifier for the query to be answered
 * @param data Data to be returned to the widget, 0-1024 bytes
 * @returns [Boolean]
 */
@TgAPI
inline fun answerWidgetQuery(widgetQueryId: String, data: String? = null) = AnswerWidgetQueryAction(widgetQueryId, data)
