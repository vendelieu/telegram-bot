package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Describes a service message about the approval of a suggested post.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostapproved)
 * @property suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property price Optional. Amount paid for the post
 * @property sendDate Date when the post will be published
 */
@Serializable
data class SuggestedPostApproved(
    val suggestedPostMessage: Message? = null,
    val price: SuggestedPostPrice? = null,
    val sendDate: Int,
)
