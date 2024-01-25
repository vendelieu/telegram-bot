package eu.vendeli.tgbot.types.internal.options

import kotlinx.serialization.Serializable

@Serializable
data class CopyMessagesOptions(
    var messageThreadId: Int? = null,
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var removeCaption: Boolean? = null,
) : Options
