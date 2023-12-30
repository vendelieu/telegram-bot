package eu.vendeli.tgbot.types.internal.options

import java.time.Instant

data class ChatInviteLinkOptions(
    var name: String? = null,
    var expireDate: Instant? = null,
    var memberLimit: Int? = null,
    var createsJoinRequest: Boolean? = null,
) : Options
