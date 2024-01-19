package eu.vendeli.tgbot.types.internal.options

data class ForwardMessageOptions(
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var messageThreadId: Int? = null,
) : Options
