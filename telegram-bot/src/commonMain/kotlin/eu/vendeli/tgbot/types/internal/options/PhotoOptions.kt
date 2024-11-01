package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class PhotoOptions(
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
    ShowCaptionAboveMediaProp
