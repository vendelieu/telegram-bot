package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Contains information about a suggested post.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostinfo)
 * @property state State of the suggested post. Currently, it can be one of "pending", "approved", "declined".
 * @property price Optional. Proposed price of the post. If the field is omitted, then the post is unpaid.
 * @property sendDate Optional. Proposed send date of the post. If the field is omitted, then the post can be published at any time within 30 days at the sole discretion of the user or administrator who approves it.
 */
@Serializable
data class SuggestedPostInfo(
    val state: String,
    val price: SuggestedPostPrice? = null,
    val sendDate: Int? = null,
)
