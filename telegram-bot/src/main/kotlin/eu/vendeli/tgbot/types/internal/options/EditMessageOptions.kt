package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode

class EditMessageOptions(
    var disableWebPagePreview: Boolean? = null,
    override var parseMode: ParseMode? = null,
) : OptionsParseMode
