package eu.vendeli.tgbot.utils.serde

import eu.vendeli.tgbot.types.internal.Identifier
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

internal object IdentifierSerializer : JsonContentPolymorphicSerializer<Identifier>(Identifier::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Identifier> {
        val content = element.jsonPrimitive.content
        return when {
            content.toLongOrNull() != null -> Identifier.Long.serializer()
            content.isNotBlank() -> Identifier.String.serializer()
            else -> error("Unsupported identifier - $content")
        }
    }
}
