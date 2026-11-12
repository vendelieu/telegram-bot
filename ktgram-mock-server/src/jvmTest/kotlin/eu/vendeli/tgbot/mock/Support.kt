package eu.vendeli.tgbot.mock

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

val TOKEN = MockServerConfig.DEFAULT_TOKEN
const val USER = 4242L

fun obj(block: kotlinx.serialization.json.JsonObjectBuilder.() -> Unit): JsonObject = buildJsonObject(block)

fun arr(vararg items: JsonElement) = JsonArray(items.toList())

fun jsonOf(value: Any?): JsonElement = when (value) {
    null -> JsonNull
    is JsonElement -> value
    is Boolean -> JsonPrimitive(value)
    is Number -> JsonPrimitive(value)
    is String -> JsonPrimitive(value)
    is Map<*, *> -> JsonObject(value.entries.associate { (k, v) -> k.toString() to jsonOf(v) })
    is List<*> -> JsonArray(value.map(::jsonOf))
    else -> error("Unsupported $value")
}

fun params(vararg pairs: Pair<String, Any?>) = JsonObject(pairs.associate { it.first to jsonOf(it.second) })

/** A clock tests can move forward by hand. */
class MutableClock(
    private var now: Instant = Instant.parse("2026-11-13T00:00:00Z"),
) : Clock() {
    override fun getZone() = ZoneOffset.UTC

    override fun withZone(zone: java.time.ZoneId?) = this

    override fun instant(): Instant = now

    fun advanceMillis(ms: Long) {
        now = now.plusMillis(ms)
    }
}

suspend fun MockTelegramServer.ok(method: String, vararg pairs: Pair<String, Any?>): JsonElement {
    val result = call(TOKEN, method, params(*pairs))
    check(result is ApiResult.Ok) { "$method failed: ${result.toJson()}" }
    return result.result
}

suspend fun MockTelegramServer.error(method: String, vararg pairs: Pair<String, Any?>): ApiException {
    val result = call(TOKEN, method, params(*pairs))
    check(result is ApiResult.Error) { "$method was expected to fail but gave ${result.toJson()}" }
    return result.error
}

fun JsonElement.o() = jsonObject

fun JsonElement.a() = jsonArray

infix fun ApiException.shouldFailWith(description: String) {
    this.description shouldBe description
}
