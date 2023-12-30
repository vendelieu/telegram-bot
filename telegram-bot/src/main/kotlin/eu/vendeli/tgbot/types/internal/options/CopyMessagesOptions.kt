package eu.vendeli.tgbot.types.internal.options

data class CopyMessagesOptions(
    var messageThreadId: Int? = null,
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var removeCaption: Boolean? = null,
) : Options
