package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Contains parameters of a post that is being suggested by the bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostparameters)
 * @property price Optional. Proposed price for the post. If the field is omitted, then the post is unpaid.
 * @property sendDate Optional. Proposed send date of the post. If specified, then the date must be between 300 second and 2678400 seconds (30 days) in the future. If the field is omitted, then the post can be published at any time within 30 days at the sole discretion of the user who approves it.
 */
@Serializable
data class SuggestedPostParameters(
    val price: SuggestedPostPrice? = null,
    val sendDate: Int? = null,
)
