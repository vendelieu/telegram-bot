package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.LinkPreviewOptions
import eu.vendeli.tgbot.types.component.ParseMode
import kotlinx.serialization.Serializable

@Serializable
class EditMessageOptions(
    override var linkPreviewOptions: LinkPreviewOptions? = null,
    override var parseMode: ParseMode? = null,
) : OptionsParseMode,
    LinkPreviewProp
