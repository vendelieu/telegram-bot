package eu.vendeli.tgbot.types.user

import kotlinx.serialization.Serializable

/**
 * This object represent a list of gifts.
 *
 * [Api reference](https://core.telegram.org/bots/api#gifts)
 * @property gifts The list of gifts
 */
@Serializable
data class Gifts(
    val gifts: List<Gift>,
)
