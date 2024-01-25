package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.inline.InlineQueryResultsButton
import kotlinx.serialization.Serializable

@Serializable
data class AnswerInlineQueryOptions(
    var cacheTime: Int? = null,
    var isPersonal: Boolean? = null,
    var nextOffset: String? = null,
    var button: InlineQueryResultsButton? = null,
) : Options
