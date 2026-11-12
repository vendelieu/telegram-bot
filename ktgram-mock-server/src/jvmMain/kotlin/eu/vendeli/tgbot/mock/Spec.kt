package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.InputStream

/** A parsed type reference of the scrapped spec ("Integer", "Array of Message", "InputFile", ...). */
sealed interface TypeRef {
    data object Str : TypeRef

    data object Int : TypeRef

    data object Bool : TypeRef

    data object Float : TypeRef

    data object InputFile : TypeRef

    data class Arr(
        val inner: TypeRef,
    ) : TypeRef

    data class Obj(
        val name: String,
    ) : TypeRef

    companion object {
        private const val ARRAY_PREFIX = "Array of "

        fun parse(raw: String): TypeRef = when {
            raw.startsWith(ARRAY_PREFIX) -> Arr(parse(raw.removePrefix(ARRAY_PREFIX)))
            raw == "String" -> Str
            raw == "Integer" -> Int
            raw == "Boolean" -> Bool
            raw == "Float" -> Float
            raw == "InputFile" -> InputFile
            else -> Obj(raw)
        }
    }
}

data class FieldSpec(
    val name: String,
    val types: List<TypeRef>,
    val isRequired: Boolean,
    val description: String,
) {
    /** Literal values quoted in the description (“private”, “group”, ...), usually the allowed enum values. */
    val quotedValues: List<String> by lazy { QUOTED.findAll(description).map { it.groupValues[1] }.toList() }

    private companion object {
        val QUOTED = Regex("“([^”]+)”")
    }
}

data class MethodSpec(
    val name: String,
    val returns: List<TypeRef>,
    val fields: List<FieldSpec>,
    val description: String = "",
) {
    val fieldsByName: Map<String, FieldSpec> by lazy { fields.associateBy { it.name } }
}

data class TypeSpec(
    val name: String,
    val fields: List<FieldSpec>,
    val subtypes: List<String>,
) {
    val fieldsByName: Map<String, FieldSpec> by lazy { fields.associateBy { it.name } }
}

/** The Bot API description the mock server validates against and generates responses from. */
class ApiSpec(
    val version: String,
    methods: Map<String, MethodSpec>,
    val types: Map<String, TypeSpec>,
) {
    private val methodsByLowercase = methods.mapKeys { it.key.lowercase() }

    /** Methods keyed by their canonical name. */
    val methods: Map<String, MethodSpec> = methods

    /** Telegram treats method names case-insensitively. */
    fun method(name: String): MethodSpec? = methodsByLowercase[name.lowercase()]

    companion object {
        const val RESOURCE = "api.json"

        /** The spec bundled into the jar from `buildSrc/src/main/resources/api.json`. */
        fun bundled(): ApiSpec {
            val stream = ApiSpec::class.java.classLoader.getResourceAsStream(RESOURCE)
                ?: error("Bundled $RESOURCE is missing from the classpath")
            return stream.use(::load)
        }

        fun load(stream: InputStream): ApiSpec = parse(stream.readBytes().toString(Charsets.UTF_8))

        fun parse(text: String): ApiSpec {
            val root = Json.parseToJsonElement(text).jsonObject
            val methods = root.getValue("methods").jsonObject.mapValues { (name, el) ->
                val obj = el.jsonObject
                MethodSpec(
                    name,
                    obj.typeList("returns"),
                    obj.fields(),
                    (obj["description"] as? JsonArray).orEmpty().joinToString(" ") { it.jsonPrimitive.content },
                )
            }
            val types = root.getValue("types").jsonObject.mapValues { (name, el) ->
                val obj = el.jsonObject
                TypeSpec(
                    name = name,
                    fields = obj.fields(),
                    subtypes = obj["subtypes"]?.jsonArray?.map { it.jsonPrimitive.content }.orEmpty(),
                )
            }
            return ApiSpec(root["version"]?.jsonPrimitive?.content.orEmpty(), methods, types)
        }

        private fun JsonObject.typeList(key: String): List<TypeRef> =
            (this[key] as? JsonArray).orEmpty().map { TypeRef.parse(it.jsonPrimitive.content) }

        private fun JsonObject.fields(): List<FieldSpec> = (this["fields"] as? JsonArray).orEmpty().map {
            val f = it.jsonObject
            FieldSpec(
                name = f.getValue("name").jsonPrimitive.content,
                types = f.typeList("types"),
                isRequired = f["required"]?.jsonPrimitive?.boolean ?: false,
                description = f["description"]?.jsonPrimitive?.content.orEmpty(),
            )
        }
    }
}
