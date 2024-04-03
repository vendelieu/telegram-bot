package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.LocationContent
import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 * Note: It is necessary to enable inline feedback via @BotFather in order to receive these objects in updates.
 *
 * Api reference: https://core.telegram.org/bots/api#choseninlineresult
 * @property resultId The unique identifier for the result that was chosen
 * @property from The user that chose the result
 * @property location Optional. Sender location, only for bots that require user location
 * @property inlineMessageId Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message. Will be also received in callback queries and can be used to edit the message.
 * @property query The query that was used to obtain the result
 */
@Serializable
data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val location: LocationContent? = null,
    val inlineMessageId: String? = null,
    val query: String,
)
