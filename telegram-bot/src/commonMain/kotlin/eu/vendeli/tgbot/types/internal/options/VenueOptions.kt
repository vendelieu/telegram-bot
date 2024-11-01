package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ReplyParameters
import kotlinx.serialization.Serializable

@Serializable
data class VenueOptions(
    var foursquareId: String? = null,
    var foursquareType: String? = null,
    var googlePlaceId: String? = null,
    var googlePlaceType: String? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
) : OptionsCommon,
    ForumProps,
    MessageEffectIdProp
