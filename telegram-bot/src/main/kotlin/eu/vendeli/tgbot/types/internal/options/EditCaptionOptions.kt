package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode

data class EditCaptionOptions(
    override var parseMode: ParseMode? = null,
) : OptionsInterface<EditCaptionOptions>, OptionsParseMode
