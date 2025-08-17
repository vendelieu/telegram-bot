package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.msg.SuggestedPostParameters
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
    override var directMessagesTopicId: Int? = null,
    override var suggestedPostParameters: SuggestedPostParameters? = null,
) : OptionsParseMode,
    ForumProps,
    OptionsCommon,
    ShowCaptionAboveMediaProp,
    AllowPaidBroadcastProp,
    DirectMessagesTopicProp,
    SuggestedPostParametersProp
