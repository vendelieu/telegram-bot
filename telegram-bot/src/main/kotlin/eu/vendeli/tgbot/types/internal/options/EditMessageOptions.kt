package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.LinkPreviewOptions
import eu.vendeli.tgbot.types.ParseMode

class EditMessageOptions(
    override var linkPreviewOptions: LinkPreviewOptions? = null,
    override var parseMode: ParseMode? = null,
) : OptionsParseMode, LinkPreviewProp
