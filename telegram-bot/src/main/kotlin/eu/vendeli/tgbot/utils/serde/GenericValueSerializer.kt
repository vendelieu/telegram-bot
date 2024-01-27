package eu.vendeli.tgbot.utils.serde

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal abstract class GenericValueSerializer<T>(private val selector: T.() -> String) : KSerializer<T> {
    override val descriptor = PrimitiveSerialDescriptor("ValueSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(selector.invoke(value))
    }

    override fun deserialize(decoder: Decoder): T = error("Not implemented")
}
