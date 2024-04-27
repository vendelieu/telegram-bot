package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about new members invited to a video chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#videochatparticipantsinvited)
 * @property users New members that were invited to the video chat
 */
@Serializable
data class VideoChatParticipantsInvited(
    val users: List<User>,
)
