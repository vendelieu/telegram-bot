package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.inline.InlineQueryResultsButton
import eu.vendeli.tgbot.types.keyboard.WebAppInfo
import kotlinx.serialization.Serializable

@Serializable
data class AnswerInlineQueryOptions(
    var cacheTime: Int? = null,
    var isPersonal: Boolean? = null,
    var nextOffset: String? = null,
    var button: InlineQueryResultsButton? = null,
) : Options {
    fun button(text: String, webAppInfo: () -> WebAppInfo) {
        button = InlineQueryResultsButton(text, webApp = webAppInfo())
    }

    fun button(text: String, startParameter: () -> String) {
        button = InlineQueryResultsButton(text, startParameter = startParameter())
    }
}
