@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.widget

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditSharedWidgetDataAction(
    widgetId: String,
    publicData: String,
    cacheTime: Int? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("editSharedWidgetData")
    override val method = "editSharedWidgetData"
    override val returnType = getReturnType()

    init {
        parameters["widget_id"] = widgetId.toJsonElement()
        parameters["public_data"] = publicData.toJsonElement()
        cacheTime?.let { parameters["cache_time"] = it.toJsonElement() }
    }
}

/**
 * Use this method to edit public data of a shared widget created by the bot. A shared widget can be edited at most once per second. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editsharedwidgetdata)
 * @param widgetId Unique identifier of the shared widget
 * @param publicData New public data of the widget, 0-1024 bytes; pass an empty string to remove the public data
 * @param cacheTime The maximum amount of time in seconds that the public data of the widget may be cached client-side; 5-3600. If omitted, then the current value is kept, which is 30 by default.
 * @returns [Boolean]
 */
@TgAPI
inline fun editSharedWidgetData(widgetId: String, publicData: String, cacheTime: Int? = null) =
    EditSharedWidgetDataAction(widgetId, publicData, cacheTime)
