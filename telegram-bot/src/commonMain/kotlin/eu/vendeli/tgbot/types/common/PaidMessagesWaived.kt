package eu.vendeli.tgbot.types.common

import kotlinx.serialization.Serializable

/**
 * Describes a service message about the sender of the message using a link of the other user, which allows sending messages to the user without paying Telegram Stars. Currently holds no information.
 *
 * [Api reference](https://core.telegram.org/bots/api#paidmessageswaived)
 */
@Serializable
class PaidMessagesWaived
