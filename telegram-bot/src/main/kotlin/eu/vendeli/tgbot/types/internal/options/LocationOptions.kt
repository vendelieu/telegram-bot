package eu.vendeli.tgbot.types.internal.options

data class LocationOptions(
    var horizontalAccuracy: Float? = null,
    var livePeriod: Int? = null,
    var heading: Int? = null,
    var proximityAlertRadius: Int? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var messageThreadId: Long? = null
) : OptionsInterface<LocationOptions>, OptionsCommon
