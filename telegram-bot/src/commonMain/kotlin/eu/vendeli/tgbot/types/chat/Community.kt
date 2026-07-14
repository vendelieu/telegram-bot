package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

/**
 * Represents a community (a group of chats).
 *
 * [Api reference](https://core.telegram.org/bots/api#community)
 * @property id Unique identifier for this community. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property name Name of the community
 */
@Serializable
data class Community(
    val id: Long,
    val name: String,
)
