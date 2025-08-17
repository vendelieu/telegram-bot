package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Describes a service message about the rejection of a suggested post.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostdeclined)
 * @property suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property comment Optional. Comment with which the post was declined
 */
@Serializable
data class SuggestedPostDeclined(
    val suggestedPostMessage: Message? = null,
    val comment: String? = null,
)
