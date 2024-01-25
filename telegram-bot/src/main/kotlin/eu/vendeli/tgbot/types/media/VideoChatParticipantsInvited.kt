package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

@Serializable
data class VideoChatParticipantsInvited(
    val users: List<User>,
)
