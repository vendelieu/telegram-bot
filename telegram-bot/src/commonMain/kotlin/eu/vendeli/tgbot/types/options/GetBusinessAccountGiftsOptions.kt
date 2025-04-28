package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class GetBusinessAccountGiftsOptions(
    var excludeUnsaved: Boolean? = null,
    var excludeSaved: Boolean? = null,
    var excludeUnlimited: Boolean? = null,
    var excludeLimited: Boolean? = null,
    var excludeUnique: Boolean? = null,
    var sortByPrice: Boolean? = null,
    var offset: String? = null,
    var limit: Int? = null,
) : Options
