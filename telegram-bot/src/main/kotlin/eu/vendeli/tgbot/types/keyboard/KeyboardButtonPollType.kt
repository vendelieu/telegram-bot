package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.types.PollType
import kotlinx.serialization.Serializable

@Serializable
data class KeyboardButtonPollType(val type: PollType? = null)
