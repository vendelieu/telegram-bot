package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
@Serializable
data class ChatInviteLinkOptions(
    var name: String? = null,
    @Serializable(InstantSerializer::class)
    var expireDate: Instant? = null,
    var memberLimit: Int? = null,
    var createsJoinRequest: Boolean? = null,
) : Options
