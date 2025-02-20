package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 *
 * [Api reference](https://core.telegram.org/bots/api#proximityalerttriggered)
 * @property traveler User that triggered the alert
 * @property watcher User that set the alert
 * @property distance The distance between the users
 */
@Serializable
data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Int,
)
