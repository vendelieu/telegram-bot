package eu.vendeli.tgbot.types.internal.options

data class MediaGroupOptions(
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<MediaGroupOptions>, IOptionsCommon
