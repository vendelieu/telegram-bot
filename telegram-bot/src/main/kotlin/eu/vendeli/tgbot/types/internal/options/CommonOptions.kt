package eu.vendeli.tgbot.types.internal.options

class CommonOptions(
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsCommon, OptionsInterface<CommonOptions>
