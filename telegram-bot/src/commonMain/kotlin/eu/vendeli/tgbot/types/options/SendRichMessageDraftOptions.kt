package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class SendRichMessageDraftOptions(
    override var messageThreadId: Int? = null,
    var canStop: Boolean? = null,
    var keepOnStop: Boolean? = null,
) : ForumProps
