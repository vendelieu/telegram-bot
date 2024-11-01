package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class LocationOptions(
    var horizontalAccuracy: Float? = null,
    var livePeriod: Int? = null,
    var heading: Int? = null,
    var proximityAlertRadius: Int? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
) : OptionsCommon,
    ForumProps,
    MessageEffectIdProp
