package eu.vendeli.tgbot.mock

import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class HttpTest :
    FunSpec({
        val server = MockTelegramServer(MockServerConfig(port = 0)).start()
        val client = HttpClient(Java)
        val api = "${server.baseUrl}/bot$TOKEN"

        fun HttpResponse.json(text: String): JsonObject = Json.parseToJsonElement(text).o()

        afterSpec {
            client.close()
            server.close()
        }

        test("JSON, query and urlencoded bodies all work and methods are case-insensitive") {
            runBlocking {
                val viaJson = client.post("$api/sendMessage") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"chat_id":$USER,"text":"json"}""")
                }
                viaJson.json(viaJson.bodyAsText())["result"]!!.o().str("text") shouldBe "json"

                val viaQuery = client.get("$api/SendMessage?chat_id=$USER&text=query")
                viaQuery.json(viaQuery.bodyAsText())["result"]!!.o().str("text") shouldBe "query"

                val viaForm = client.submitForm(
                    "$api/sendMessage",
                    parameters {
                        append("chat_id", "$USER")
                        append("text", "form")
                        append("reply_markup", """{"inline_keyboard":[[{"text":"b","callback_data":"d"}]]}""")
                    },
                )
                viaForm.json(viaForm.bodyAsText())["result"]!!.o()["reply_markup"] shouldBe
                    jsonOf(mapOf("inline_keyboard" to listOf(listOf(mapOf("text" to "b", "callback_data" to "d")))))
            }
        }

        test("errors use the Telegram envelope and status codes") {
            runBlocking {
                val badToken = client.get("${server.baseUrl}/botbad/getMe")
                badToken.status.value shouldBe 401
                badToken.bodyAsText() shouldContain "\"error_code\":401"

                client.get("$api/nope").status.value shouldBe 404
                client.get("${server.baseUrl}/elsewhere").status.value shouldBe 404

                val invalid = client.post("$api/sendMessage") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"chat_id":1}""")
                }
                invalid.status.value shouldBe 400
                invalid.bodyAsText() shouldContain "message text is empty"

                val broken = client.post("$api/sendMessage") {
                    contentType(ContentType.Application.Json)
                    setBody("{not json")
                }
                broken.status.value shouldBe 400
            }
        }

        test("the test environment path /test/ is accepted") {
            runBlocking {
                client.get("${server.baseUrl}/bot$TOKEN/test/getMe").status.value shouldBe 200
            }
        }

        test("multipart uploads can be fetched with getFile and downloaded") {
            runBlocking {
                val response = client.submitFormWithBinaryData(
                    "$api/sendDocument",
                    formData {
                        append("chat_id", "$USER")
                        append(
                            "document",
                            "binary-payload".toByteArray(),
                            Headers.build {
                                append(HttpHeaders.ContentType, "application/octet-stream")
                                append(HttpHeaders.ContentDisposition, "filename=\"data.bin\"")
                            },
                        )
                    },
                )
                val doc = response.json(response.bodyAsText())["result"]!!.o()["document"]!!.o()
                doc.str("file_name") shouldBe "data.bin"
                val file = client.get("$api/getFile?file_id=${doc.str("file_id")}")
                val path = file.json(file.bodyAsText())["result"]!!.o().str("file_path")
                client.get("${server.baseUrl}/file/bot$TOKEN/$path").bodyAsBytes().decodeToString() shouldBe
                    "binary-payload"
                client.get("${server.baseUrl}/file/bot$TOKEN/documents/missing.bin").status.value shouldBe 404
            }
        }

        test("the control API drives the bot over HTTP") {
            runBlocking {
                val control = "${server.baseUrl}/__mock/bots/$TOKEN"
                client.delete(control)
                client.get("${server.baseUrl}/__mock/health").bodyAsText() shouldContain "\"ok\":true"

                client
                    .post("$control/messages") {
                        contentType(ContentType.Application.Json)
                        setBody("""{"text":"/start","from_id":$USER,"username":"ann"}""")
                    }.status.value shouldBe 200
                val updates = client.get("$api/getUpdates").let { it.json(it.bodyAsText())["result"]!!.a() }
                updates shouldHaveSize 1
                updates
                    .first()
                    .o()["message"]!!
                    .o()["from"]!!
                    .o()
                    .str("username") shouldBe "ann"

                client.post("$api/sendMessage") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"chat_id":$USER,"text":"hello"}""")
                }
                val calls = client
                    .get(
                        "$control/calls?method=sendMessage",
                    ).let { it.json(it.bodyAsText())["result"]!!.a() }
                calls shouldHaveSize 1
                val messages = client
                    .get(
                        "$control/chats/$USER/messages",
                    ).let { it.json(it.bodyAsText())["result"]!!.a() }
                messages shouldHaveSize 2

                client
                    .post("$control/callbacks") {
                        contentType(ContentType.Application.Json)
                        setBody("""{"chat_id":$USER,"message_id":2,"data":"btn"}""")
                    }.status.value shouldBe 200

                client.post("$control/faults") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"method":"getMe","status":429,"retry_after":7}""")
                }
                val limited = client.get("$api/getMe")
                limited.status.value shouldBe 429
                limited.bodyAsText() shouldContain "\"retry_after\":7"

                client.post("$control/stubs") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"method":"getMe","result":{"id":1,"is_bot":true,"first_name":"S"}}""")
                }
                client.get("$api/getMe").bodyAsText() shouldContain "\"first_name\":\"S\""

                client.post("$control/block") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"user_id":$USER}""")
                }
                client
                    .post("$api/sendMessage") {
                        contentType(ContentType.Application.Json)
                        setBody("""{"chat_id":$USER,"text":"x"}""")
                    }.status.value shouldBe 403

                client.delete(control).status.value shouldBe 200
                client.get("$control/calls").let { it.json(it.bodyAsText())["result"]!!.a() } shouldHaveSize 0
                client.get("$control/unknown").status.value shouldBe 404
            }
        }
    })
