package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class AnswerCallbackQueryOptions(
    var text: String? = null,
    var showAlert: Boolean? = null,
    var url: String? = null,
    var cacheTime: Int? = null,
) : Options
