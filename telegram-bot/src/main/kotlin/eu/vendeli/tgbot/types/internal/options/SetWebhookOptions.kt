package eu.vendeli.tgbot.types.internal.options

import java.io.File

data class SetWebhookOptions(
    var certificate: File? = null,
    var ipAddress: String? = null,
    var maxConnections: Int? = null,
    var allowedUpdates: List<String>? = null,
    var dropPendingUpdates: Boolean? = null,
    var secretToken: String? = null,
) : OptionsInterface<SetWebhookOptions>
