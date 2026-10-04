package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import kotlinx.serialization.Serializable

@Serializable
data class EditEphemeralMessageCaptionOptions(
    override var parseMode: ParseMode? = null,
    override var showCaptionAboveMedia: Boolean? = null,
) : OptionsParseMode,
    ShowCaptionAboveMediaProp
