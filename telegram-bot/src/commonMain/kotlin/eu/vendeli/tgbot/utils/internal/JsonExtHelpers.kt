package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.utils.common.serde
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonPrimitive

@Suppress("NOTHING_TO_INLINE")
internal inline fun Boolean.toJsonElement() = JsonPrimitive(this)

@Suppress("NOTHING_TO_INLINE")
internal inline fun String.toJsonElement() = JsonPrimitive(this)

@Suppress("NOTHING_TO_INLINE")
internal inline fun Number.toJsonElement() = JsonPrimitive(this)

internal inline fun <T : Any, reified S : KSerializer<T>> T.encodeWith(serializer: S) =
    serde.encodeToJsonElement(serializer, this)

internal inline fun <T : Any, reified S : KSerializer<T>> List<T>.encodeWith(serializer: S) =
    serde.encodeToJsonElement(ListSerializer(serializer), this)
