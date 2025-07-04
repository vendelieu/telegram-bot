package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class StickerOptions(
    var emoji: String? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
) : OptionsCommon,
    ForumProps,
    MessageEffectIdProp,
    AllowPaidBroadcastProp
