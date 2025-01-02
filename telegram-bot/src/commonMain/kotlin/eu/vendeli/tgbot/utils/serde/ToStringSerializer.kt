package eu.vendeli.tgbot.utils.serde

import eu.vendeli.tgbot.utils.TgException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal abstract class ToStringSerializer<T>(
    private val selector: T.() -> String,
) : KSerializer<T> {
    override val descriptor = PrimitiveSerialDescriptor("String serializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(selector.invoke(value))
    }

    override fun deserialize(decoder: Decoder): T = throw TgException("Not implemented")
}
