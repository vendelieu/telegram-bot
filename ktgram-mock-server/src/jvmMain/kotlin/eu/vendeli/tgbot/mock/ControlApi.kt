package eu.vendeli.tgbot.mock

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveText
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Duration.Companion.milliseconds

/**
 * HTTP twin of [BotHandle], for tests written in other languages:
 *
 * - `GET  /__mock/health`
 * - `POST /__mock/bots/{token}/messages`   `{text, from_id?, chat_id?, chat_type?, chat_title?, first_name?, username?}`
 * - `POST /__mock/bots/{token}/callbacks`  `{chat_id, message_id, data, from_id?}`
 * - `POST /__mock/bots/{token}/inline`     `{query, from_id?, offset?}`
 * - `POST /__mock/bots/{token}/updates`    a raw Update object
 * - `GET  /__mock/bots/{token}/calls[?method=]`
 * - `GET  /__mock/bots/{token}/chats/{id}/messages`
 * - `POST /__mock/bots/{token}/stubs`      `{method, result | error, match?, times?}`
 * - `POST /__mock/bots/{token}/faults`     `{method?, status, description, retry_after?, times?, delay_ms?}`
 * - `POST /__mock/bots/{token}/block`, `DELETE /__mock/bots/{token}/block`   `{user_id}`
 * - `DELETE /__mock/bots/{token}`          reset the bot
 */
internal class ControlApi(
    private val server: MockTelegramServer,
) {
    suspend fun handle(call: ApplicationCall, path: String) {
        val result = try {
            route(call, call.request.httpMethod.value, path.trim('/').split('/'))
        } catch (e: ApiException) {
            ApiResult.Error(e)
        } catch (e: SerializationException) {
            ApiResult.Error(ApiException.badRequest("invalid JSON: ${e.message?.lineSequence()?.first()}"))
        } catch (e: IllegalStateException) {
            ApiResult.Error(ApiException.badRequest(e.message.orEmpty()))
        }
        with(server) { call.respondApi(result) }
    }

    private suspend fun route(call: ApplicationCall, verb: String, parts: List<String>): ApiResult {
        if (parts == listOf("health")) return ApiResult.Raw(HTTP_OK, server.health())
        if (parts.size < 2 || parts[0] != "bots") throw ApiException.notFound()
        val bot = server.bot(parts[1])
        val action = parts.getOrNull(2)
        return when {
            action == null && verb == "DELETE" -> {
                bot.reset().let { ok(TRUE) }
            }

            action == "messages" && verb == "POST" -> {
                body(call).let { ok(userMessage(bot, it)) }
            }

            action == "callbacks" && verb == "POST" -> {
                body(call).let { ok(callback(bot, it)) }
            }

            action == "inline" && verb == "POST" -> {
                body(call).let {
                    ok(
                        bot.inlineQuery(
                            it.str("query").orEmpty(),
                            it.long("from_id") ?: BotHandle.DEFAULT_USER_ID,
                            it.str("offset").orEmpty(),
                        ),
                    )
                }
            }

            action == "updates" && verb == "POST" -> {
                ok(bot.enqueue(body(call)) ?: JsonNull)
            }

            action == "calls" && verb == "GET" -> {
                ok(calls(bot, call.request.queryParameters["method"]))
            }

            action == "chats" && parts.getOrNull(4) == "messages" && verb == "GET" -> {
                ok(JsonArray(bot.messages(parts[3].toLong())))
            }

            action == "stubs" && verb == "POST" -> {
                body(call).let { stub(bot, it) }.let { ok(TRUE) }
            }

            action == "faults" && verb == "POST" -> {
                body(call).let { fault(bot, it) }.let { ok(TRUE) }
            }

            action == "block" -> {
                body(call).let { b ->
                    val id = b.long("user_id") ?: throw ApiException.badRequest("user_id is empty")
                    if (verb == "DELETE") bot.unblockedBy(id) else bot.blockedBy(id)
                    ok(TRUE)
                }
            }

            else -> {
                throw ApiException.notFound()
            }
        }
    }

    private fun ok(value: JsonElement) = ApiResult.Raw(
        HTTP_OK,
        buildJsonObject {
            put("ok", true)
            put("result", value)
        },
    )

    private suspend fun body(call: ApplicationCall): JsonObject {
        val text = call.receiveText()
        if (text.isBlank()) return JsonObject(emptyMap())
        return Json.parseToJsonElement(text) as? JsonObject ?: throw ApiException.badRequest("JSON object is expected")
    }

    private fun userMessage(bot: BotHandle, b: JsonObject): JsonObject {
        val from = b.long("from_id") ?: BotHandle.DEFAULT_USER_ID
        return bot.userSays(
            text = b.str("text") ?: throw ApiException.badRequest("text is empty"),
            fromId = from,
            chatId = b.long("chat_id") ?: from,
            firstName = b.str("first_name") ?: "Test",
            username = b.str("username"),
            chatType = b.str("chat_type") ?: "private",
            chatTitle = b.str("chat_title"),
            replyToMessageId = b.long("reply_to_message_id"),
        )
    }

    private fun callback(bot: BotHandle, b: JsonObject): JsonObject {
        val chat = b.long("chat_id") ?: throw ApiException.badRequest("chat_id is empty")
        val message = b.long("message_id") ?: throw ApiException.badRequest("message_id is empty")
        return bot.pressButton(chat, message, b.str("data").orEmpty(), b.long("from_id") ?: chat)
    }

    private fun calls(bot: BotHandle, method: String?) = JsonArray(
        bot.calls(method).map {
            buildJsonObject {
                put("method", it.method)
                put("params", it.params)
                put("status", it.status)
                put("response", it.response)
            }
        },
    )

    private fun stub(bot: BotHandle, b: JsonObject) {
        val method = b.str("method") ?: throw ApiException.badRequest("method is empty")
        val match = b["match"] as? JsonObject ?: JsonObject(emptyMap())
        val times = b.long("times")?.toInt() ?: Int.MAX_VALUE
        val error = b["error"] as? JsonObject
        if (error != null) {
            bot.stubError(
                method,
                ApiException(error.long("error_code")?.toInt() ?: HTTP_BAD_REQUEST, error.str("description").orEmpty()),
                match,
                times,
            )
        } else {
            bot.stub(method, b["result"] ?: TRUE, match, times)
        }
    }

    private fun fault(bot: BotHandle, b: JsonObject) {
        val status = b.long("status")?.toInt() ?: HTTP_BAD_REQUEST
        val description = b.str("description").orEmpty()
        val retryAfter = b.long("retry_after")?.toInt()
        val error = if (status == HTTP_TOO_MANY_REQUESTS && retryAfter != null) {
            ApiException.tooManyRequests(retryAfter)
        } else {
            ApiException(status, description)
        }
        bot.fail(b.str("method"), error, b.long("times")?.toInt() ?: 1, (b.long("delay_ms") ?: 0L).milliseconds)
    }
}
