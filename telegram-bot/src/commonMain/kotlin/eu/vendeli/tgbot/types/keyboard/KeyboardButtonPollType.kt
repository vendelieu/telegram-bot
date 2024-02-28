package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.types.PollType
import kotlinx.serialization.Serializable

/**
 * This object represents type of a poll, which is allowed to be created and sent when the corresponding button is pressed.
 * @property type Optional. If quiz is passed, the user will be allowed to create only polls in the quiz mode. If regular is passed, only regular polls will be allowed. Otherwise, the user will be allowed to create a poll of any type.
 * Api reference: https://core.telegram.org/bots/api#keyboardbuttonpolltype
*/
@Serializable
data class KeyboardButtonPollType(val type: PollType? = null)
