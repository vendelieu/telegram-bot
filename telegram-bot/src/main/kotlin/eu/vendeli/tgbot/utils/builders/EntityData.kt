package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.User

data class EntityData(
    var url: String? = null,
    var user: User? = null,
    var language: String? = null,
)
