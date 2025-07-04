package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class PaidMediaOptions(
    var payload: String? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var parseMode: ParseMode? = null,
    override var showCaptionAboveMedia: Boolean? = null,
    override var allowPaidBroadcast: Boolean? = null,
) : OptionsCommon,
    OptionsParseMode,
    ShowCaptionAboveMediaProp,
    AllowPaidBroadcastProp
