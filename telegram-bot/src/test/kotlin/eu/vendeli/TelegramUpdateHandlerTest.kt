package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.parseCommand
import io.kotest.matchers.maps.shouldContainExactly
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
        bot.config.apply {
            commandParsing.restrictSpacesInCommands = true
        }
        val deeplinkParse = bot.update.parseCommand("/start deeplinkcode")
        deeplinkParse.command shouldBe "/start"
        deeplinkParse.params shouldContainExactly (mapOf("param_1" to "deeplinkcode"))

        val commandParseWithNoParams = bot.update.parseCommand("/command")
        commandParseWithNoParams.command shouldBe "/command"
        commandParseWithNoParams.params.size shouldBe 0

        val commandParseWithOneEmptyParam = bot.update.parseCommand("/command?")
        commandParseWithOneEmptyParam.command shouldBe "/command"
        commandParseWithOneEmptyParam.params shouldContainExactly (mapOf("param_1" to ""))

        val commandParseWithMixedParams = bot.update.parseCommand("command?p1=v1&v2&p3=&p4=v4&p5=")
        commandParseWithMixedParams.command shouldBe "command"
        commandParseWithMixedParams.params shouldContainExactly (
            mapOf(
                "p1" to "v1",
                "param_2" to "v2",
                "p3" to "",
                "p4" to "v4",
                "p5" to "",
            )
            )

        val commandParseForLastFullPair = bot.update.parseCommand("last_pair_command?v1&p2=v2")
        commandParseForLastFullPair.command shouldBe "last_pair_command"
        commandParseForLastFullPair.params shouldContainExactly (mapOf("param_1" to "v1", "p2" to "v2"))

        bot.config.commandParsing.apply {
            commandDelimiter = '_'
        }

        val underscoreCommand = bot.update.parseCommand("/test_123")
        underscoreCommand.command shouldBe "/test"
        underscoreCommand.params shouldContainExactly (mapOf("param_1" to "123"))

        bot.config.commandParsing.apply {
            commandDelimiter = ' '
            parametersDelimiter = ' '
            parameterValueDelimiter = ' '
            restrictSpacesInCommands = false
        }
        val deeplinkCheck = bot.update.parseCommand("/start bafefdf0-64cb-47da-97f0-4a1f11d469a2")
        deeplinkCheck.command shouldBe "/start"
        deeplinkCheck.params shouldContainExactly (mapOf("param_1" to "bafefdf0-64cb-47da-97f0-4a1f11d469a2"))
    }

    @Test
    suspend fun `deeplink test`() {
        val deeplinkUpdate = Update(
            updateId = 1,
            message = Message(
                messageId = 2,
                from = User(id = 3, isBot = false, firstName = "test"),
                date = 1682227456,
                chat = Chat(id = 4, type = ChatType.Private),
                text = "/start test",
                entities = listOf(MessageEntity(type = EntityType.BotCommand, offset = 0, length = 6)),
            ),
        )
        bot.httpClient = HttpClient(
            MockEngine {
                respond(
                    content = ByteReadChannel(mapper.writeValueAsString(Response.Success(listOf(deeplinkUpdate)))),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
        )

        bot.handleUpdates {
            onCommand("/start") {
                parameters shouldContainExactly mapOf("deepLink" to "test")
            }
            bot.update.stopListener()
        }
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
