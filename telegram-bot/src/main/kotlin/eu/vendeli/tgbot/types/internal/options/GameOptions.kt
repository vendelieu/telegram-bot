package eu.vendeli.tgbot.types.internal.options

data class GameOptions(
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<GameOptions>, OptionsCommon
