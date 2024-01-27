package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

@Serializable(Identifier.Serde::class)
sealed class Identifier {
    abstract val get: Any

    @Serializable
    data class String(val to: kotlin.String) : Identifier() {
        override val get: kotlin.String get(): kotlin.String = to
    }

    @Serializable
    data class Long(val to: kotlin.Long) : Identifier() {
        override val get: kotlin.Long get(): kotlin.Long = to
    }

    companion object {
        fun from(recipient: kotlin.Long) = Long(recipient)
        fun from(recipient: kotlin.String) = String(recipient)

        fun from(recipient: User) = Long(recipient.id)
    }

    internal object Serde : JsonContentPolymorphicSerializer<Identifier>(Identifier::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Identifier> {
            val content = element.jsonPrimitive.content
            return when {
                content.toLongOrNull() != null -> Long.serializer()
                content.isNotBlank() -> String.serializer()
                else -> error("Unsupported identifier - $content")
            }
        }
    }
}
