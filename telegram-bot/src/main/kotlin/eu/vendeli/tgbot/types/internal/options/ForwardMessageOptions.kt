package eu.vendeli.tgbot.types.internal.options

data class ForwardMessageOptions(
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var messageThreadId: Long?
) : OptionsInterface<ForwardMessageOptions>
