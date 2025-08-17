package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Describes a service message about the failed approval of a suggested post. Currently, only caused by insufficient user funds at the time of approval.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostapprovalfailed)
 * @property suggestedPostMessage Optional. Message containing the suggested post whose approval has failed. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property price Expected price of the post
 */
@Serializable
data class SuggestedPostApprovalFailed(
    val suggestedPostMessage: Message? = null,
    val price: SuggestedPostPrice,
)
