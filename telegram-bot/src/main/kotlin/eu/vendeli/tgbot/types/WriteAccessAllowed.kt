package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user allowing a bot added to the attachment menu to write messages.
 * Currently, holds no information.
 */
@Serializable
data class WriteAccessAllowed(
    val fromRequest: Boolean? = null,
    val fromAttachmentMenu: Boolean? = null,
    val webAppName: String? = null,
)
