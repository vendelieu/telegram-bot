package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.UpdateType

data class SetWebhookOptions(
    var certificate: InputFile? = null,
    var ipAddress: String? = null,
    var maxConnections: Int? = null,
    var allowedUpdates: List<UpdateType>? = null,
    var dropPendingUpdates: Boolean? = null,
    var secretToken: String? = null,
) : Options
