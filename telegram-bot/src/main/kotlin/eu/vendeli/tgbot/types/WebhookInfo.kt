package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.UpdateType
import java.time.Instant

data class WebhookInfo(
    val url: String,
    val hasCustomCertificate: Boolean,
    val pendingUpdateCount: Int,
    val ipAddress: String? = null,
    val lastErrorDate: Instant? = null,
    val lastErrorMessage: String? = null,
    val lastSynchronizationErrorDate: Instant? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null,
)
