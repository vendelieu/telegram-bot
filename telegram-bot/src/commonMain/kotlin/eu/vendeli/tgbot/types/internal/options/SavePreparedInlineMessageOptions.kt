package eu.vendeli.tgbot.types.internal.options

import kotlinx.serialization.Serializable

@Serializable
data class SavePreparedInlineMessageOptions(
    var allowUserChats: Boolean? = null,
    var allowBotChats: Boolean? = null,
    var allowGroupChats: Boolean? = null,
    var allowChannelChats: Boolean? = null,
) : Options
