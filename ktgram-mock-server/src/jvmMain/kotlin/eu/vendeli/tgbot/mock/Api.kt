package eu.vendeli.tgbot.mock

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/** An error the Bot API would answer with, `{"ok":false,"error_code":..,"description":..}`. */
class ApiException(
    val code: Int,
    val description: String,
    val parameters: JsonObject? = null,
) : RuntimeException("$code: $description") {
    companion object {
        fun badRequest(description: String) = ApiException(HTTP_BAD_REQUEST, "Bad Request: $description")

        fun forbidden(description: String) = ApiException(HTTP_FORBIDDEN, "Forbidden: $description")

        fun notFound() = ApiException(HTTP_NOT_FOUND, "Not Found")

        fun unauthorized() = ApiException(HTTP_UNAUTHORIZED, "Unauthorized")

        fun conflict(description: String) = ApiException(HTTP_CONFLICT, "Conflict: $description")

        fun tooManyRequests(retryAfter: Int) = ApiException(
            HTTP_TOO_MANY_REQUESTS,
            "Too Many Requests: retry after $retryAfter",
            buildJsonObject { put("retry_after", retryAfter) },
        )
    }
}

/** The outcome of one Bot API call, ready to be written to the wire. */
sealed interface ApiResult {
    val status: Int

    data class Ok(
        val result: JsonElement,
    ) : ApiResult {
        override val status = HTTP_OK
    }

    data class Error(
        val error: ApiException,
    ) : ApiResult {
        override val status = error.code
    }

    /** A pre-recorded or stubbed response body returned verbatim. */
    data class Raw(
        override val status: Int,
        val body: JsonElement,
    ) : ApiResult

    fun toJson(): JsonElement = when (this) {
        is Ok -> buildJsonObject {
            put("ok", true)
            put("result", result)
        }

        is Error -> buildJsonObject {
            put("ok", false)
            put("error_code", error.code)
            put("description", error.description)
            error.parameters?.let { put("parameters", it) }
        }

        is Raw -> body
    }
}

internal const val HTTP_OK = 200
internal const val HTTP_BAD_REQUEST = 400
internal const val HTTP_UNAUTHORIZED = 401
internal const val HTTP_FORBIDDEN = 403
internal const val HTTP_NOT_FOUND = 404
internal const val HTTP_CONFLICT = 409
internal const val HTTP_TOO_MANY_REQUESTS = 429

internal val TRUE: JsonElement = JsonPrimitive(true)

internal fun JsonObject.str(key: String): String? = (this[key] as? JsonPrimitive)
    ?.takeUnless {
        it is JsonNull
    }?.content

internal fun JsonObject.long(key: String): Long? = str(key)?.toLongOrNull()

internal fun JsonObject.bool(key: String): Boolean? = str(key)?.toBooleanStrictOrNull()

internal fun JsonObject.with(vararg entries: Pair<String, JsonElement>): JsonObject = JsonObject(this + entries)

internal fun JsonObject.without(vararg keys: String): JsonObject = JsonObject(filterKeys { it !in keys })
