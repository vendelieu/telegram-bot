package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

/**
 * This object defines the parameters for the creation of a managed bot. Information about the created bot will be shared with the bot using the update managed_bot and a Message with the field managed_bot_created.
 *
 * [Api reference](https://core.telegram.org/bots/api#keyboardbuttonrequestmanagedbot)
 * @property requestId Signed 32-bit identifier of the request. Must be unique within the message
 * @property suggestedName Optional. Suggested name for the bot
 * @property suggestedUsername Optional. Suggested username for the bot
 */
@Serializable
data class KeyboardButtonRequestManagedBot(
    val requestId: Int,
    val suggestedName: String? = null,
    val suggestedUsername: String? = null,
)
