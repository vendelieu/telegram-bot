package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user allowing a bot to write messages after adding it to the attachment menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess.
 *
 * [Api reference](https://core.telegram.org/bots/api#writeaccessallowed)
 * @property fromRequest Optional. True, if the access was granted after the user accepted an explicit request from a Web App sent by the method requestWriteAccess
 * @property webAppName Optional. Name of the Web App, if the access was granted when the Web App was launched from a link
 * @property fromAttachmentMenu Optional. True, if the access was granted when the bot was added to the attachment or side menu
 */
@Serializable
data class WriteAccessAllowed(
    val fromRequest: Boolean? = null,
    val fromAttachmentMenu: Boolean? = null,
    val webAppName: String? = null,
)
