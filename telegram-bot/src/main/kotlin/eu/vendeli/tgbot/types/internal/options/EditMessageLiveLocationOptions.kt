package eu.vendeli.tgbot.types.internal.options

data class EditMessageLiveLocationOptions(
    var horizontalAccuracy: Float? = null,
    var heading: Int? = null,
    var proximityAlertRadius: Int? = null
) : OptionsInterface<EditMessageLiveLocationOptions>
