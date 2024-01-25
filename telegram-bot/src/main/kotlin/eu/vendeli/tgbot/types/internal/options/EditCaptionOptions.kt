package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import kotlinx.serialization.Serializable

@Serializable
data class EditCaptionOptions(
    override var parseMode: ParseMode? = null,
) : OptionsParseMode
