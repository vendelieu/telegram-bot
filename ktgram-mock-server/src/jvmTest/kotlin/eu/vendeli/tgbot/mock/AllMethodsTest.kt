package eu.vendeli.tgbot.mock

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** Every method of the scrapped spec must be callable and answer a schema-conformant result. */
class AllMethodsTest :
    FunSpec({
        val server = MockTelegramServer(MockServerConfig(port = 0))
        val spec = server.spec
        val generator = Generator(spec, MutableClock())

        fun minimalParams(method: MethodSpec): JsonObject = JsonObject(
            method.fields
                .filter { it.isRequired }
                .mapNotNull { f ->
                    val ref = f.types.first()
                    val value: JsonElement? = when {
                        f.name == "chat_id" || f.name == "from_chat_id" -> JsonPrimitive(USER)
                        f.name == "message_id" -> JsonPrimitive(1)
                        ref == TypeRef.InputFile -> JsonPrimitive("https://example.com/a.bin")
                        ref is TypeRef.Obj && f.types.size > 1 && f.types.any { it == TypeRef.Int } -> JsonPrimitive(1)
                        else -> generator.generate(ref, f.name)
                    }
                    value?.let { f.name to it }
                }.toMap(),
        )

        /** Required fields of every object in [value] are present, recursively, following the declared types. */
        fun conforms(value: JsonElement, ref: TypeRef, path: String): List<String> = when {
            ref is TypeRef.Arr && value is JsonArray -> {
                value.flatMapIndexed {
                    i,
                    el,
                    ->
                    conforms(el, ref.inner, "$path[$i]")
                }
            }

            ref is TypeRef.Obj && value is JsonObject -> {
                val type = spec.types[ref.name]
                when {
                    type == null -> {
                        emptyList()
                    }

                    type.subtypes.isNotEmpty() -> {
                        val tries = type.subtypes.map { conforms(value, TypeRef.Obj(it), path) }
                        if (tries.any { it.isEmpty() }) emptyList() else tries.first()
                    }

                    else -> {
                        type.fields.flatMap { f ->
                            val v = value[f.name]
                            when {
                                v == null || v is JsonNull -> if (f.isRequired) listOf(
                                    "$path.${f.name} missing",
                                ) else emptyList()

                                else -> f.types.map { conforms(v, it, "$path.${f.name}") }.firstOrNull { it.isEmpty() }
                                    ?: f.types.map { conforms(v, it, "$path.${f.name}") }.first()
                            }
                        }
                    }
                }
            }

            ref is TypeRef.Str || ref is TypeRef.InputFile -> {
                if (value is JsonPrimitive) emptyList() else listOf("$path not a string")
            }

            ref is TypeRef.Int || ref is TypeRef.Float -> {
                if (value is JsonPrimitive) emptyList() else listOf("$path not a number")
            }

            ref is TypeRef.Bool -> {
                if (value is JsonPrimitive) emptyList() else listOf("$path not a boolean")
            }

            else -> {
                listOf("$path has unexpected shape ${value::class.simpleName} for $ref")
            }
        }

        spec.methods.values.forEach { method ->
            test("${method.name} answers a conformant response") {
                runBlocking {
                    val result = server.call(TOKEN, method.name, minimalParams(method))
                    when (result) {
                        is ApiResult.Ok -> {
                            val refs = method.returns
                            val problems = refs.map { conforms(result.result, it, method.name) }
                            (problems.any { it.isEmpty() }) shouldBe true
                        }

                        // Deliberate, Telegram-worded refusals (unknown message to edit, wrong file id, ...).
                        is ApiResult.Error -> {
                            result.error.description.shouldNotBeEmpty()
                            (result.error.code in setOf(400, 403)) shouldBe true
                        }

                        is ApiResult.Raw -> {
                            error("unexpected raw result")
                        }
                    }
                }
            }
        }
    })
