package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.types.component.UpdateType
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Describes the current status of a webhook.
 *
 * [Api reference](https://core.telegram.org/bots/api#webhookinfo)
 * @property url Webhook URL, may be empty if webhook is not set up
 * @property hasCustomCertificate True, if a custom certificate was provided for webhook certificate checks
 * @property pendingUpdateCount Number of updates awaiting delivery
 * @property ipAddress Optional. Currently used webhook IP address
 * @property lastErrorDate Optional. Unix time for the most recent error that happened when trying to deliver an update via webhook
 * @property lastErrorMessage Optional. Error message in human-readable format for the most recent error that happened when trying to deliver an update via webhook
 * @property lastSynchronizationErrorDate Optional. Unix time of the most recent error that happened when trying to synchronize available updates with Telegram datacenters
 * @property maxConnections Optional. The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
 * @property allowedUpdates Optional. A list of update types the bot is subscribed to. Defaults to all update types except chat_member
 */
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
