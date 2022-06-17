package eu.vendeli.tgbot.types.internal.options

data class VenueOptions(
    var foursquareId: String? = null,
    var foursquareType: String? = null,
    var googlePlaceId: String? = null,
    var googlePlaceType: String? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<VenueOptions>, IOptionsCommon
