package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.common.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class SendChecklistOptions(
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageEffectId: String? = null,
) : OptionsCommon,
    MessageEffectIdProp
