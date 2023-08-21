package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.internal.InputFile

data class SetWebhookOptions(
    var certificate: InputFile? = null,
    var ipAddress: String? = null,
    var maxConnections: Int? = null,
    var allowedUpdates: List<String>? = null,
    var dropPendingUpdates: Boolean? = null,
    var secretToken: String? = null,
) : OptionsInterface<SetWebhookOptions>
