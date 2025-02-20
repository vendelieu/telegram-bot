package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.component.ImplicitFile
import kotlinx.serialization.Serializable

@Serializable
data class AudioOptions(
    override var parseMode: ParseMode? = null,
    var duration: Int? = null,
    var performer: String? = null,
    var title: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
    override var thumbnail: ImplicitFile? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
) : OptionsCommon,
    ForumProps,
    OptionsParseMode,
    ThumbnailProp,
    MessageEffectIdProp
