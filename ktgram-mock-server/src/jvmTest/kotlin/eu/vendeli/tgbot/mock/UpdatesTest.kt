package eu.vendeli.tgbot.mock

import com.sun.net.httpserver.HttpServer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.net.InetSocketAddress
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class UpdatesTest :
    FunSpec({
        fun newServer() = MockTelegramServer(MockServerConfig(port = 0, webhookRetryDelay = 50.milliseconds))

        test("getUpdates serves queued updates, commands get their entity, offset confirms") {
            newServer().use { server ->
                runBlocking {
                    server.ok("getUpdates").a() shouldHaveSize 0
                    server.bot().userSays("/start now", fromId = USER)
                    server.bot().userSays("second", fromId = USER)
                    val updates = server.ok("getUpdates").a()
                    updates.map { it.o().long("update_id") } shouldBe listOf(1L, 2L)
                    updates
                        .first()
                        .o()["message"]!!
                        .o()["entities"]!!
                        .a()
                        .first()
                        .o()
                        .str("type") shouldBe
                        "bot_command"
                    server.ok("getUpdates").a() shouldHaveSize 2 // not confirmed yet
                    server.ok("getUpdates", "offset" to 2).a().map { it.o().long("update_id") } shouldBe listOf(2L)
                    server.ok("getUpdates", "offset" to 3).a() shouldHaveSize 0
                }
            }
        }

        test("limit and negative offset") {
            newServer().use { server ->
                runBlocking {
                    repeat(5) { server.bot().userSays("m$it", fromId = USER) }
                    server.ok("getUpdates", "limit" to 2).a() shouldHaveSize 2
                    server.ok("getUpdates", "offset" to -2).a().map { it.o().long("update_id") } shouldBe listOf(4L, 5L)
                }
            }
        }

        test("long polling wakes up as soon as an update arrives") {
            newServer().use { server ->
                runBlocking {
                    val pending = async { server.ok("getUpdates", "timeout" to 10).a() }
                    delay(150)
                    server.bot().userSays("wake up", fromId = USER)
                    withTimeout(3.seconds) { pending.await() } shouldHaveSize 1
                }
            }
        }

        test("long polling returns empty after the timeout") {
            newServer().use { server ->
                runBlocking {
                    val started = System.currentTimeMillis()
                    server.ok("getUpdates", "timeout" to 1).a() shouldHaveSize 0
                    (System.currentTimeMillis() - started >= 900) shouldBe true
                }
            }
        }

        test("a second poller terminates the first with 409") {
            newServer().use { server ->
                runBlocking {
                    val first = async { server.call(TOKEN, "getUpdates", params("timeout" to 10)) }
                    delay(150)
                    val second = async { server.call(TOKEN, "getUpdates", params("timeout" to 1)) }
                    val firstResult = withTimeout(3.seconds) { first.await() } as ApiResult.Error
                    firstResult.error.code shouldBe 409
                    firstResult.error.description shouldContain "terminated by other getUpdates request"
                    (second.await() is ApiResult.Ok) shouldBe true
                }
            }
        }

        test("getUpdates is refused while a webhook is set") {
            newServer().use { server ->
                runBlocking {
                    server.ok("setWebhook", "url" to "http://localhost:1/hook")
                    server.error("getUpdates").code shouldBe 409
                    server.ok("getWebhookInfo").o().str("url") shouldBe "http://localhost:1/hook"
                    server.ok("deleteWebhook")
                    server.ok("getWebhookInfo").o().str("url") shouldBe ""
                    server.ok("getUpdates").a() shouldHaveSize 0
                }
            }
        }

        test("allowed_updates filters update types, chat_member is off by default") {
            newServer().use { server ->
                runBlocking {
                    val bot = server.bot()
                    bot.enqueue(obj { put("chat_member", obj { }) }) shouldBe null
                    server.ok("getUpdates", "allowed_updates" to listOf("callback_query"))
                    bot.userSays("filtered", fromId = USER)
                    server.ok("getUpdates").a() shouldHaveSize 0
                    server.ok("getUpdates", "allowed_updates" to listOf("message", "chat_member"))
                    bot.enqueue(obj { put("chat_member", obj { }) })!!
                    server.ok("getUpdates").a() shouldHaveSize 1
                }
            }
        }

        test("webhook delivery posts updates with the secret header and drains the queue") {
            val received = CopyOnWriteArrayList<Pair<String?, String>>()
            val receiver = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0).apply {
                createContext("/hook") { ex ->
                    received +=
                        ex.requestHeaders.getFirst("X-Telegram-Bot-Api-Secret-Token") to
                        ex.requestBody.readBytes().decodeToString()
                    ex.sendResponseHeaders(200, -1)
                    ex.close()
                }
                start()
            }
            try {
                newServer().use { server ->
                    runBlocking {
                        server.ok(
                            "setWebhook",
                            "url" to "http://127.0.0.1:${receiver.address.port}/hook",
                            "secret_token" to "s3cret",
                        )
                        server.bot().userSays("via webhook", fromId = USER)
                        withTimeout(5.seconds) { while (received.isEmpty()) delay(20) }
                        received.single().first shouldBe "s3cret"
                        received.single().second shouldContain "via webhook"
                        withTimeout(5.seconds) {
                            while (server.ok("getWebhookInfo").o().long("pending_update_count") != 0L) delay(20)
                        }
                        server
                            .error(
                                "setWebhook",
                                "url" to "http://x/",
                                "secret_token" to "bad token!",
                            ).description shouldContain
                            "illegal"
                    }
                }
            } finally {
                receiver.stop(0)
            }
        }

        test("failed webhook deliveries are reported and retried") {
            val hits = AtomicInteger()
            val receiver = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0).apply {
                createContext("/hook") { ex ->
                    ex.requestBody.readBytes()
                    ex.sendResponseHeaders(if (hits.incrementAndGet() == 1) 500 else 200, -1)
                    ex.close()
                }
                start()
            }
            try {
                newServer().use { server ->
                    runBlocking {
                        server.ok("setWebhook", "url" to "http://127.0.0.1:${receiver.address.port}/hook")
                        server.bot().userSays("retry me", fromId = USER)
                        withTimeout(5.seconds) { while (hits.get() < 2) delay(20) }
                        withTimeout(5.seconds) {
                            while (server.ok("getWebhookInfo").o().long("pending_update_count") != 0L) delay(20)
                        }
                        server.ok("getWebhookInfo").o().str("last_error_message") shouldBe
                            "Wrong response from the webhook: 500"
                    }
                }
            } finally {
                receiver.stop(0)
            }
        }

        test("https can be required for webhooks") {
            MockTelegramServer(MockServerConfig(port = 0, requireHttpsWebhook = true)).use { server ->
                runBlocking {
                    server.error("setWebhook", "url" to "http://a.b/") shouldFailWith
                        "Bad Request: bad webhook: An HTTPS URL must be provided for webhook"
                }
            }
        }
    })
