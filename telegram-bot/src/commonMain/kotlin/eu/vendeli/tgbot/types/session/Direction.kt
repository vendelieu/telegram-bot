package eu.vendeli.tgbot.types.session

/** Flow direction of a tracked message relative to the bot. */
enum class Direction {
    /** Received from the user / chat. */
    Incoming,

    /** Sent by the bot. */
    Outgoing,
}
