package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class WebhookInfo(
    val url: String,
    val hasCustomCertificate: Boolean,
    val pendingUpdateCount: Int,
    val ipAddress: String? = null,
    @Serializable(InstantSerializer::class)
    val lastErrorDate: Instant? = null,
    val lastErrorMessage: String? = null,
    @Serializable(InstantSerializer::class)
    val lastSynchronizationErrorDate: Instant? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null,
)
