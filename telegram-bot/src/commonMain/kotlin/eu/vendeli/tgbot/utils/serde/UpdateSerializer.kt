package eu.vendeli.tgbot.utils.serde

import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.component.ProcessedUpdate
import eu.vendeli.tgbot.utils.common.cast
import eu.vendeli.tgbot.utils.common.processUpdate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal abstract class UpdateSerializer<T : ProcessedUpdate> : KSerializer<T> {
    override val descriptor = Update.serializer().descriptor
    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeSerializableValue(Update.serializer(), value.origin)
    }

    override fun deserialize(decoder: Decoder): T = Update
        .serializer()
        .deserialize(decoder)
        .processUpdate()
        .cast()
}
