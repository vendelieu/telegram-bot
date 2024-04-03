package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a change in auto-delete timer settings.
 *
 * Api reference: https://core.telegram.org/bots/api#messageautodeletetimerchanged
 * @property messageAutoDeleteTime New auto-delete time for messages in the chat; in seconds
 */
@Serializable
data class MessageAutoDeleteTimerChanged(
    val messageAutoDeleteTime: Int,
)
