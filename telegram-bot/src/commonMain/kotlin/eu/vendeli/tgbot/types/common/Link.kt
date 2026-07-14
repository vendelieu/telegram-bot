package eu.vendeli.tgbot.types.common

import kotlinx.serialization.Serializable

/**
 * Represents an HTTP link.
 *
 * [Api reference](https://core.telegram.org/bots/api#link)
 * @property url URL of the link
 */
@Serializable
data class Link(
    val url: String,
)
