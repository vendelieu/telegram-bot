package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class ForwardMessagesOptions(
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var messageThreadId: Int? = null,
    override var directMessagesTopicId: Int? = null,
) : Options,
    DirectMessagesTopicProp
