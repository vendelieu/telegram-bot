package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.Update
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class TelegramUpdateHandlerTest : BotTestContext() {
    @Test
    suspend fun `listener workflow`() {
        bot.httpClient = HttpClient(
            MockEngine {
                respond(
                    content = ByteReadChannel(updates),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        var update: Update? = null

        bot.update.setListener {
            update = it
            stopListener()
        }

        update.shouldNotBeNull()
        update?.updateId shouldBe 53192527
        update?.message?.from?.username shouldBe "username"
        update?.message?.from?.firstName shouldBe "John Doe"
    }

    @Test
    suspend fun `exception catching via manual handling`() {
        doMockHttp()

        bot.update.caughtExceptions.tryReceive().getOrNull().shouldBeNull()

        bot.handleUpdates {
            onMessage {
                throw NoSuchElementException("test")
            }
            bot.update.stopListener()
        }
        val throwableUpdatePair = bot.update.caughtExceptions.tryReceive().getOrNull()
        throwableUpdatePair.shouldNotBeNull()

        throwableUpdatePair.first shouldNotBeSameInstanceAs NoSuchElementException::class
        throwableUpdatePair.first.message shouldBe "test"

        throwableUpdatePair.second.message.shouldNotBeNull()
        throwableUpdatePair.second.message?.text shouldBe "/start"
    }

    @Test
    suspend fun `exception catching via annotation handling`() {
        doMockHttp("test")

        bot.update.caughtExceptions.tryReceive().getOrNull().shouldBeNull()
        bot.update.setListener {
            handle(it)
            stopListener()
        }

        val throwableUpdatePair = bot.update.caughtExceptions.tryReceive().getOrNull()
        throwableUpdatePair.shouldNotBeNull()

        throwableUpdatePair.first shouldNotBeSameInstanceAs IllegalArgumentException::class
        throwableUpdatePair.first.message shouldBe "test2"

        throwableUpdatePair.second.message.shouldNotBeNull()
        throwableUpdatePair.second.message?.text shouldBe "test"
    }

    companion object {
        val updates = """
            {"ok":true,"result":[{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},
            "chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start",
            "entities":[{"offset":0,"length":6,"type":"bot_command"}]}}]}
        """.trimIndent()
    }
}
