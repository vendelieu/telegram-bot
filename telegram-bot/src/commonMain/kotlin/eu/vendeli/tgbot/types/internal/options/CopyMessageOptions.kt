package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CopyMessageOptions(
    @Serializable(DurationSerializer::class)
    var videoStartTimestamp: Duration? = null,
    override var showCaptionAboveMedia: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
    override var allowPaidBroadcast: Boolean? = null,
) : OptionsParseMode,
    ForumProps,
    OptionsCommon,
    ShowCaptionAboveMediaProp
