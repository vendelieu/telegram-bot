package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import kotlinx.serialization.Serializable

@Serializable
data class EditCaptionOptions(
    override var showCaptionAboveMedia: Boolean? = null,
    override var parseMode: ParseMode? = null,
) : OptionsParseMode,
    ShowCaptionAboveMediaProp
