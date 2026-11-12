package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

/** Checks request parameters against the scrapped spec and reports the first problem the way Telegram words it. */
class Validator(
    private val spec: ApiSpec,
) {
    fun validate(method: MethodSpec, params: JsonObject) {
        for (field in method.fields) {
            val value = params[field.name]
            if (value == null || value is JsonNull) {
                if (field.isRequired) throw ApiException.badRequest(emptyMessage(method, field))
                continue
            }
            if (field.types.none { matches(value, it, MAX_DEPTH) }) {
                throw ApiException.badRequest("wrong type of the field \"${field.name}\", ${describe(field)} expected")
            }
        }
    }

    private fun emptyMessage(method: MethodSpec, field: FieldSpec): String = when {
        // Wordings of the real Bot API server (Client.cpp): each argument parser has its own text.
        field.name == "text" && method.name in TEXT_METHODS -> "message text is empty"

        field.name == "user_id" || field.name.endsWith("_user_id") -> "invalid ${field.name} specified"

        field.name in MEDIA_FIELDS && method.name.startsWith("send") -> "there is no ${field.name} in the request"

        else -> "${field.name} is empty"
    }

    private fun describe(field: FieldSpec) = field.types.joinToString(" or ") { it.display() }

    private fun TypeRef.display(): String = when (this) {
        TypeRef.Str -> "String"
        TypeRef.Int -> "Integer"
        TypeRef.Bool -> "Boolean"
        TypeRef.Float -> "Float"
        TypeRef.InputFile -> "InputFile"
        is TypeRef.Arr -> "Array of ${inner.display()}"
        is TypeRef.Obj -> name
    }

    private fun matches(value: JsonElement, ref: TypeRef, depth: Int): Boolean = when (ref) {
        TypeRef.Str, TypeRef.InputFile -> value is JsonPrimitive && value !is JsonNull

        // Telegram coerces numeric strings, the library sends some integers (colors, ids) that way.
        TypeRef.Int -> value is JsonPrimitive && value.longOrNull != null

        TypeRef.Float -> value is JsonPrimitive && value.doubleOrNull != null

        TypeRef.Bool -> value is JsonPrimitive && !value.isString && value.booleanOrNull != null

        is TypeRef.Arr -> value is JsonArray && value.size <= MAX_ARRAY && value.all { matches(it, ref.inner, depth) }

        is TypeRef.Obj -> value is JsonObject && matchesObject(value, ref.name, depth)
    }

    private fun matchesObject(value: JsonObject, typeName: String, depth: Int): Boolean {
        if (depth <= 0) return true
        val type = spec.types[typeName] ?: return true
        if (type.subtypes.isNotEmpty()) return type.subtypes.any { matchesObject(value, it, depth) }
        return type.fields.all { field ->
            val v = value[field.name]
            when {
                v == null || v is JsonNull -> !field.isRequired
                else -> field.types.any { matches(v, it, depth - 1) }
            }
        }
    }

    private companion object {
        const val MAX_DEPTH = 6
        const val MAX_ARRAY = 10_000
        val TEXT_METHODS = setOf("sendMessage", "editMessageText")
        val MEDIA_FIELDS = setOf("photo", "audio", "document", "video", "animation", "voice", "video_note", "sticker")
    }
}
