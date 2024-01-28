package eu.vendeli.tgbot.utils

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.serializer

@Suppress("NOTHING_TO_INLINE")
internal inline fun Boolean.toJsonElement() = JsonPrimitive(this)

@Suppress("NOTHING_TO_INLINE")
internal inline fun String.toJsonElement() = JsonPrimitive(this)

@Suppress("NOTHING_TO_INLINE")
internal inline fun Number.toJsonElement() = JsonPrimitive(this)

@OptIn(InternalSerializationApi::class)
@Suppress("UnusedReceiverParameter")
internal inline fun <reified T : Any> List<T>.toJsonElement() =
    serde.encodeToJsonElement(ListSerializer(T::class.serializer()))

@OptIn(InternalSerializationApi::class)
internal inline fun <reified T : Any> T.toJsonElement() = serde.encodeToJsonElement(T::class.serializer())
