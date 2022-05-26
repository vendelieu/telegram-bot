package com.github.vendelieu.tgbot.types.internal.options

data class ForwardMessageOptions(
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null
) : OptionsInterface<ForwardMessageOptions>
