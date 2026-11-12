package eu.vendeli.tgbot.mock

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class SpecAndValidationTest :
    FunSpec({
        val server = MockTelegramServer(MockServerConfig(port = 0))

        test("the bundled spec is the scrapped Bot API description") {
            val spec = ApiSpec.bundled()
            spec.methods.size shouldBeGreaterThan 150
            spec.types.size shouldBeGreaterThan 300
            spec.method("SENDMESSAGE")?.name shouldBe "sendMessage"
            spec
                .method("sendMessage")!!
                .fieldsByName
                .getValue("chat_id")
                .types shouldContain TypeRef.Int
        }

        test("a missing required parameter is reported like Telegram does") {
            runBlocking {
                server.error("sendMessage", "chat_id" to 1) shouldFailWith "Bad Request: message text is empty"
                server.error("sendMessage", "text" to "x") shouldFailWith "Bad Request: chat_id is empty"
            }
        }

        test("a parameter of the wrong type is rejected") {
            runBlocking {
                server.error("sendMessage", "chat_id" to listOf(1), "text" to "x").code shouldBe 400
                server.error("getChat", "chat_id" to true).code shouldBe 400
            }
        }

        test("nested required fields are validated") {
            runBlocking {
                val bad = params(
                    "chat_id" to 1,
                    "text" to "x",
                    "reply_markup" to mapOf("keyboard_typo" to listOf<Any>()),
                )
                (server.call(TOKEN, "sendMessage", bad) as ApiResult.Error).error.code shouldBe 400
            }
        }

        test("an unknown method is 404 and a malformed token is 401") {
            runBlocking {
                (server.call(TOKEN, "noSuchMethod") as ApiResult.Error).error.code shouldBe 404
                (server.call("not-a-token", "getMe") as ApiResult.Error).error.code shouldBe 401
            }
        }

        test("allowed tokens restrict who may call") {
            MockTelegramServer(MockServerConfig(allowedTokens = setOf(TOKEN))).use {
                runBlocking {
                    (it.call("999:" + "A".repeat(35), "getMe") as ApiResult.Error).error.code shouldBe 401
                    (it.call(TOKEN, "getMe") is ApiResult.Ok) shouldBe true
                }
            }
        }
    })
