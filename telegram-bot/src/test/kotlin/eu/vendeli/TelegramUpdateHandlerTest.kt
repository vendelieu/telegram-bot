package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.utils.parseCommand
import io.kotest.matchers.maps.shouldHaveSize
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

    @Test
    fun `valid command parsing`() {
        val deeplinkParse = bot.update.parseCommand("/start deeplinkcode")
        deeplinkParse.command shouldBe "/start"
        deeplinkParse.params.size shouldBe 1
        deeplinkParse.params.entries.first().key shouldBe "param_1"
        deeplinkParse.params.entries.first().value shouldBe "deeplinkcode"

        val commandParse = bot.update.parseCommand("command?p1=v1&v2&p3=v3")
        commandParse.command shouldBe "command"
        commandParse.params.size shouldBe 3

        val params = commandParse.params.entries
        params.first().key shouldBe "p1"
        params.first().value shouldBe "v1"

        params.elementAt(1).key shouldBe "param_2"
        params.elementAt(1).value shouldBe "v2"

        params.elementAt(2).key shouldBe "p3"
        params.elementAt(2).value shouldBe "v3"

        bot.config.commandParsing.apply {
            commandDelimiter = '_'
        }

        val underscoreCommand = bot.update.parseCommand("/test_123")
        underscoreCommand.command shouldBe "/test"
        underscoreCommand.params.size shouldBe 1
        underscoreCommand.params.entries.first().key shouldBe "param_1"
        underscoreCommand.params.entries.first().value shouldBe "123"

        bot.config.commandParsing.apply {
            commandDelimiter = ' '
            parametersDelimiter = ' '
            parameterValueDelimiter = ' '
            restrictSpacesInCommands = false
        }
        val deeplinkCheck = bot.update.parseCommand("/start bafefdf0-64cb-47da-97f0-4a1f11d469a2")
        deeplinkCheck.command shouldBe "/start"
        deeplinkCheck.params shouldHaveSize 1
        deeplinkCheck.params.entries.first().key shouldBe "param_1"
        deeplinkCheck.params.entries.first().value shouldBe "bafefdf0-64cb-47da-97f0-4a1f11d469a2"
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
