package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Result of a chat join request query processing.
 */
@Serializable
enum class JoinRequestQueryResult {
    /** Allow the user to join the chat. */
    @SerialName("approve")
    Approve,

    /** Disallow the user to join the chat. */
    @SerialName("decline")
    Decline,

    /** Leave the decision to other administrators. */
    @SerialName("queue")
    Queue,
}
