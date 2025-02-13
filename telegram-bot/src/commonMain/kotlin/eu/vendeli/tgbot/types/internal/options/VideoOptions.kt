package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class VideoOptions(
    var duration: Int? = null,
    var height: Int? = null,
    var width: Int? = null,
    var supportsStreaming: Boolean? = null,
    var cover: ImplicitFile? = null,
    @Serializable(DurationSerializer::class)
    var startTimestamp: Duration? = null,
    override var thumbnail: ImplicitFile? = null,
    override var showCaptionAboveMedia: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var hasSpoiler: Boolean? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
) : OptionsCommon,
    ForumProps,
    OptionsParseMode,
    MediaSpoiler,
    MessageEffectIdProp,
    ThumbnailProp,
    ShowCaptionAboveMediaProp
