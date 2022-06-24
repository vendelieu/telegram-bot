package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ImplicitFile

data class AudioOptions(
    override var parseMode: ParseMode? = null,
    var duration: Int? = null,
    var performer: String? = null,
    var title: String? = null,
    var thumb: ImplicitFile<*>? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<AudioOptions>, OptionsCommon, OptionsParseMode
