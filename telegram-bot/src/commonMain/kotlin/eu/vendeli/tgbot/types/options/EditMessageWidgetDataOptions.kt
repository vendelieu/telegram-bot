package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class EditMessageWidgetDataOptions(
    var publicData: String? = null,
    var chatData: String? = null,
) : Options
