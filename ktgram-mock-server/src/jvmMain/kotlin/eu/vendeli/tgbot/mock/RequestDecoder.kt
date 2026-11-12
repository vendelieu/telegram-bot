package eu.vendeli.tgbot.mock

import io.ktor.http.ContentType
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.contentType
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.receiveText
import io.ktor.utils.io.toByteArray
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

private val INTEGER = Regex("^-?\\d+$")

/** Turns a raw form/query string into the JSON value the field's declared type expects. */
internal fun coerce(raw: String, field: FieldSpec?): JsonElement {
    val types = field?.types ?: return JsonPrimitive(raw)
    val first = types.first()
    return when {
        TypeRef.Int in types && INTEGER.matches(raw) && (TypeRef.Str in types || types.size == 1) -> {
            JsonPrimitive(raw.toLong())
        }

        TypeRef.Str in types || TypeRef.InputFile in types -> {
            JsonPrimitive(raw)
        }

        first == TypeRef.Int -> {
            raw.toLongOrNull()?.let(::JsonPrimitive) ?: JsonPrimitive(raw)
        }

        first == TypeRef.Float -> {
            raw.toDoubleOrNull()?.let(::JsonPrimitive) ?: JsonPrimitive(raw)
        }

        first == TypeRef.Bool -> {
            raw.toBooleanStrictOrNull()?.let(::JsonPrimitive) ?: JsonPrimitive(raw)
        }

        else -> {
            runCatching { Json.parseToJsonElement(raw) }.getOrDefault(JsonPrimitive(raw))
        }
    }
}

/** Reads query, JSON, urlencoded and multipart bodies the same way the Bot API accepts them. */
internal object RequestDecoder {
    suspend fun decode(call: ApplicationCall, method: MethodSpec?): RequestParams {
        val values = LinkedHashMap<String, JsonElement>()
        val uploads = LinkedHashMap<String, UploadedFile>()
        fun field(name: String) = method?.fieldsByName?.get(name)

        call.request.queryParameters
            .entries()
            .forEach { (k, v) -> values[k] = coerce(v.last(), field(k)) }
        val type = call.request.contentType()
        when {
            type.match(ContentType.Application.Json) -> {
                readJson(call.receiveText(), values)
            }

            type.match(ContentType.Application.FormUrlEncoded) -> {
                call.receiveParameters().entries().forEach { (k, v) -> values[k] = coerce(v.last(), field(k)) }
            }

            type.match(ContentType.MultiPart.FormData) -> {
                call.receiveMultipart().forEachPart { part ->
                    try {
                        when (part) {
                            is PartData.FormItem -> part.name?.let { values[it] = coerce(part.value, field(it)) }

                            is PartData.FileItem -> part.name?.let {
                                uploads[it] =
                                    UploadedFile(
                                        it,
                                        part.originalFileName,
                                        part.contentType?.toString(),
                                        part.provider().toByteArray(),
                                    )
                                values[it] = JsonPrimitive("attach://$it")
                            }

                            else -> Unit
                        }
                    } finally {
                        part.dispose()
                    }
                }
            }
        }
        return RequestParams(JsonObject(values), uploads)
    }

    private fun readJson(text: String, into: MutableMap<String, JsonElement>) {
        if (text.isBlank()) return
        val parsed = try {
            Json.parseToJsonElement(text)
        } catch (e: SerializationException) {
            throw ApiException.badRequest("can't parse JSON object: ${e.message?.lineSequence()?.first()}")
        }
        val obj = parsed as? JsonObject ?: throw ApiException.badRequest("JSON object is expected")
        into.putAll(obj)
    }
}
