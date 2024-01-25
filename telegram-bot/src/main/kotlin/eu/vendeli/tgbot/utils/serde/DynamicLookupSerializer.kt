package eu.vendeli.tgbot.utils.serde

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

@ExperimentalSerializationApi
internal object DynamicLookupSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = ContextualSerializer(Any::class, null, emptyArray()).descriptor

    @OptIn(InternalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Any) {
        if (value is ArrayList<*>) {
            encoder.encodeSerializableValue(ListSerializer(DynamicLookupSerializer), value)
            return
        }
        val actualSerializer = encoder.serializersModule.getContextual(value::class) ?: value::class.serializer()
        @Suppress("UNCHECKED_CAST")
        encoder.encodeSerializableValue(actualSerializer as KSerializer<Any>, value)
    }

    override fun deserialize(decoder: Decoder): Any {
        error("Unsupported")
    }
}
