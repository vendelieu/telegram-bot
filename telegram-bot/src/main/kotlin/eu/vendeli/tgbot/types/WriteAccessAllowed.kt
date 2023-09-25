package eu.vendeli.tgbot.types

/**
 * This object represents a service message about a user allowing a bot added to the attachment menu to write messages.
 * Currently, holds no information.
 */
data class WriteAccessAllowed(
    val fromRequest: Boolean? = null,
    val fromAttachmentMenu: Boolean? = null,
    val webAppName: String? = null,
)
