package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.UpdateType
import kotlinx.serialization.Serializable

@Serializable
data class GetUpdatesOptions(
    var offset: Int? = null,
    var limit: Int? = null,
    var timeout: Int? = null,
    var allowedUpdates: List<UpdateType>? = null,
) : Options
