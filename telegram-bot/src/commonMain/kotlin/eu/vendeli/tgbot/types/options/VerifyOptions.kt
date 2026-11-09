package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOptions(
    var customDescriptionParseMode: ParseMode? = null,
) : Options
