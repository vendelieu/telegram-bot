package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.component.UpdateType
import kotlinx.serialization.Serializable

@Serializable
data class SetWebhookOptions(
    var certificate: InputFile? = null,
    var ipAddress: String? = null,
    var maxConnections: Int? = null,
    var allowedUpdates: List<UpdateType>? = null,
    var dropPendingUpdates: Boolean? = null,
    var secretToken: String? = null,
) : Options
