package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import eu.vendeli.tgbot.types.internal.ImplicitFile
import kotlinx.serialization.Serializable

@Serializable
data class AnimationOptions(
    var duration: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var thumbnail: ImplicitFile? = null,
    override var showCaptionAboveMedia: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var hasSpoiler: Boolean? = null,
    override var messageEffectId: String? = null,
) : OptionsCommon,
    OptionsParseMode,
    MediaSpoiler,
    MessageEffectIdProp,
    ShowCaptionAboveMediaProp
