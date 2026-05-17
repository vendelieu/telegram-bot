package eu.vendeli.tgbot.types.component

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.common.TgException
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

    @Serializable(Literal.Companion::class)
    data class Literal(
        val to: String,
    ) : Identifier() {
        override val get: String get(): String = to

        internal companion object : KSerializer<Literal> {
            override val descriptor = PrimitiveSerialDescriptor("String serde", PrimitiveKind.STRING)
            override fun serialize(encoder: Encoder, value: Literal) {
                encoder.encodeString(value.to)
            }

            override fun deserialize(decoder: Decoder): Literal = Literal(decoder.decodeString())
        }
    }

    @Serializable(Numeric.Companion::class)
    data class Numeric(
        val to: Long,
    ) : Identifier() {
        override val get: Long get(): Long = to

        internal companion object : KSerializer<Numeric> {
            override val descriptor = PrimitiveSerialDescriptor("Long serde", PrimitiveKind.LONG)
            override fun serialize(encoder: Encoder, value: Numeric) {
                encoder.encodeLong(value.to)
            }

            override fun deserialize(decoder: Decoder): Numeric = Numeric(decoder.decodeLong())
        }
    }

    companion object {
        fun from(recipient: Long) = Numeric(recipient)
        fun from(recipient: String) = Literal(recipient)

        fun from(recipient: User) = Numeric(recipient.id)
        fun from(recipient: Chat) = Numeric(recipient.id)
    }

    internal object Serde : JsonContentPolymorphicSerializer<Identifier>(Identifier::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Identifier> {
            val content = element.jsonPrimitive.content
            return when {
                content.toLongOrNull() != null -> Numeric.serializer()
                content.isNotBlank() -> Literal.serializer()
                else -> throw TgException("Unsupported identifier - $content")
            }
        }
    }
}
