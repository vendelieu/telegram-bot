package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageDraftOptions(
    override var parseMode: ParseMode? = null,
    override var messageThreadId: Int? = null,
) : ForumProps,
    OptionsParseMode
