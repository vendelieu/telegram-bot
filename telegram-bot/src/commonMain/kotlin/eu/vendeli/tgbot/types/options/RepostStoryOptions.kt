package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class RepostStoryOptions(
    var postToChatPage: Boolean? = null,
    var protectContent: Boolean? = null,
) : Options

