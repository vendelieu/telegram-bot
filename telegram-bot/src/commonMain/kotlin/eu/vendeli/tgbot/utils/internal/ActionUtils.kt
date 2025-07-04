package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.action.TgAction
import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.escapeIfNeeded
import kotlinx.io.Buffer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.jvm.JvmName
import kotlin.reflect.KMutableProperty0

@OptIn(InternalSerializationApi::class)
@Suppress("UnusedReceiverParameter")
internal inline fun <reified Type : Any> TgAction<Type>.getReturnType(): KSerializer<Type> = Type::class.serializer()

@Suppress("UnusedReceiverParameter")
@OptIn(InternalSerializationApi::class)
@JvmName("listReturnType")
internal inline fun <reified Type : MultipleResponse> TgAction<List<Type>>.getReturnType(): KSerializer<List<Type>> =
    ListSerializer(Type::class.serializer())

internal inline fun <R : Any> TgAction<R>.handleImplicitFile(input: ImplicitFile, fieldName: String) {
    parameters[fieldName] = input.transform(multipartData).file.toJsonElement()
}

internal inline fun <R : Any> TgAction<R>.handleImplicitFile(parameter: KMutableProperty0<ImplicitFile?>) {
    parameter.get()?.let {
        parameters[parameter.name] = it.transform(multipartData).file.toJsonElement()
    }
}

@Suppress("DEPRECATION_ERROR", "UNCHECKED_CAST")
internal inline fun <T : ImplicitMediaData, R : Any, S : KSerializer<T>> MediaAction<R>.handleImplicitFileGroup(
    input: List<T>,
    fieldName: String = "media",
    serializer: S = DynamicLookupSerializer as S,
) {
    parameters[fieldName] = buildList {
        input.forEach {
            if (it.media is ImplicitFile.Str && it.thumbnail is ImplicitFile.Str) {
                add(it.encodeWith(serializer as KSerializer<T>))
                return@forEach
            }
            it.media = it.media.transform(multipartData)
            it.thumbnail = it.thumbnail?.transform(multipartData)
            add(it.encodeWith(serializer as KSerializer<T>))
        }
    }.encodeWith(JsonElement.serializer())
}

internal inline fun ImplicitFile.transform(multiParts: MutableList<PartData.BinaryItem>): ImplicitFile.Str {
    if (this is ImplicitFile.Str) return file.toImplicitFile()
    val media = file as InputFile
    multiParts += media.toPartData(media.fileName)

    return "attach://${media.fileName}".toImplicitFile()
}

internal fun InputFile.toPartData(name: String) = PartData.BinaryItem(
    {
        Buffer().apply {
            write(data, startIndex = 0, endIndex = data.size)
        }
    },
    {},
    Headers.build {
        append(HttpHeaders.ContentDisposition, "form-data; name=${name.escapeIfNeeded()}")
        append(HttpHeaders.ContentDisposition, "filename=$fileName")
        append(HttpHeaders.ContentType, contentType)
        append(HttpHeaders.ContentLength, data.size.toString())
    },
)
