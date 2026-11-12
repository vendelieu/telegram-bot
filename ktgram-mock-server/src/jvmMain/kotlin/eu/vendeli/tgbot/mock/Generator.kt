package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.Clock

/**
 * Builds schema-valid sample objects straight from the scrapped spec: every required field is present and
 * typed, enum-like strings take the first value quoted in the field description.
 */
class Generator(
    private val spec: ApiSpec,
    private val clock: Clock,
) {
    fun generate(ref: TypeRef, fieldName: String = "", stack: List<String> = emptyList()): JsonElement? = when (ref) {
        TypeRef.Str -> JsonPrimitive(sampleString(fieldName))
        TypeRef.Int -> JsonPrimitive(sampleInt(fieldName))
        TypeRef.Bool -> JsonPrimitive(false)
        TypeRef.Float -> JsonPrimitive(0.0)
        TypeRef.InputFile -> JsonPrimitive("mock_file_id")
        is TypeRef.Arr -> JsonArray(emptyList())
        is TypeRef.Obj -> generateObject(ref.name, stack)
    }

    /** A sample of the named type with [overrides] applied on top, or null when the type recurses into itself. */
    fun generateObject(
        typeName: String,
        stack: List<String> = emptyList(),
        overrides: Map<String, JsonElement> = emptyMap(),
    ): JsonObject? {
        if (typeName in stack) return null
        val type = spec.types[typeName] ?: return JsonObject(overrides)
        type.subtypes.firstOrNull()?.let { return generateObject(it, stack + typeName, overrides) }
        val fields = LinkedHashMap<String, JsonElement>()
        for (field in type.fields) {
            val override = overrides[field.name]
            if (override != null) {
                fields[field.name] = override
            } else if (field.isRequired) {
                generateField(field, stack + typeName)?.let { fields[field.name] = it }
            }
        }
        overrides.forEach { (k, v) -> fields.putIfAbsent(k, v) }
        return JsonObject(fields)
    }

    /** The default result of a method that has no dedicated handler. */
    fun forMethod(method: MethodSpec): JsonElement {
        if (method.returns.size > 1 || method.returns.firstOrNull() == TypeRef.Bool) return TRUE
        val ref = method.returns.firstOrNull() ?: return TRUE
        return generate(ref, "") ?: TRUE
    }

    private fun generateField(field: FieldSpec, stack: List<String>): JsonElement? {
        val ref = field.types.first()
        if (ref == TypeRef.Str && field.quotedValues.isNotEmpty()) return JsonPrimitive(field.quotedValues.first())
        return generate(ref, field.name, stack)
    }

    private fun sampleString(name: String): String = when {
        name == "file_id" -> "mock_file_id"
        name == "file_unique_id" -> "mock_unique_id"
        name.endsWith("first_name") -> "Mock"
        name == "username" -> "mock_user"
        name.endsWith("url") -> "https://example.com"
        name == "text" || name.endsWith("_text") -> "text"
        name == "title" -> "Mock"
        name == "emoji" -> "🎲"
        else -> "mock"
    }

    private fun sampleInt(name: String): Long = when {
        name == "date" || name.endsWith("_date") -> clock.millis() / MILLIS_IN_SECOND
        name == "width" || name == "height" -> DEFAULT_DIMENSION
        name == "file_size" -> DEFAULT_FILE_SIZE
        name.endsWith("_count") || name == "offset" -> 0
        else -> 1
    }

    private companion object {
        const val MILLIS_IN_SECOND = 1000L
        const val DEFAULT_DIMENSION = 100L
        const val DEFAULT_FILE_SIZE = 1024L
    }
}
