package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a Web App on behalf of a user.
 *
 * Api reference: https://core.telegram.org/bots/api#sentwebappmessage
 * @property inlineMessageId Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
 */
@Serializable
data class SentWebAppMessage(val inlineMessageId: String? = null)
