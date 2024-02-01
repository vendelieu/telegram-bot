package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

@Serializable(Identifier.Serde::class)
sealed class Identifier {
    abstract val get: Any

    @Serializable(String.Companion::class)
    data class String(val to: kotlin.String) : Identifier() {
        override val get: kotlin.String get(): kotlin.String = to
        internal companion object : KSerializer<String> {
            override val descriptor = PrimitiveSerialDescriptor("String serde", PrimitiveKind.LONG)
            override fun serialize(encoder: Encoder, value: String) {
                encoder.encodeString(value.to)
            }

            override fun deserialize(decoder: Decoder): String = String(decoder.decodeString())
        }
    }

    @Serializable(Long.Companion::class)
    data class Long(val to: kotlin.Long) : Identifier() {
        override val get: kotlin.Long get(): kotlin.Long = to

        internal companion object : KSerializer<Long> {
            override val descriptor = PrimitiveSerialDescriptor("Long serde", PrimitiveKind.LONG)
            override fun serialize(encoder: Encoder, value: Long) {
                encoder.encodeLong(value.to)
            }

            override fun deserialize(decoder: Decoder): Long = Long(decoder.decodeLong())
        }
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
