package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.internal.UpdateType
import kotlinx.serialization.Serializable

@Serializable
data class GetUpdatesOptions(
    var offset: Int? = null,
    var limit: Int? = null,
    var timeout: Int? = null,
    var allowedUpdates: List<UpdateType>? = null,
) : Options
