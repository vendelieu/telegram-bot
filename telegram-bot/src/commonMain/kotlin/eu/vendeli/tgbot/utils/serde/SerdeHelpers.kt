package eu.vendeli.tgbot.utils.serde

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

internal val JsonElement.primitiveOrNull: JsonPrimitive?
    get() = this as? JsonPrimitive
