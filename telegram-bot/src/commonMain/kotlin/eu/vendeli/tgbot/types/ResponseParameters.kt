package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * Describes why a request was unsuccessful.
 * Api reference: https://core.telegram.org/bots/api#responseparameters
 * @property migrateToChatId Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property retryAfter Optional. In case of exceeding flood control, the number of seconds left to wait before the request can be repeated
*/
@Serializable
data class ResponseParameters(
    val migrateToChatId: Long? = null,
    val retryAfter: Int? = null,
)
