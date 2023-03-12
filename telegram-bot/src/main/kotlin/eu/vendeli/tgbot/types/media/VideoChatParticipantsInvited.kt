package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.User

data class VideoChatParticipantsInvited(
    val users: List<User>,
)
