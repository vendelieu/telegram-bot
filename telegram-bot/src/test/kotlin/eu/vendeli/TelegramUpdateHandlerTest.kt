package eu.vendeli

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.Update
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TelegramUpdateHandlerTest {
    private lateinit var bot: TelegramBot

    @BeforeAll
    fun prepareTestBot() {
        bot = TelegramBot(System.getenv("BOT_TOKEN"))
    }

    @Test
    fun `parsing test`(): Unit = runBlocking {
        val parsedUpdates = bot.update.parseUpdates(updates)
        assertEquals(53192527, parsedUpdates?.first()?.updateId)
        assertEquals(10831, parsedUpdates?.first()?.message?.messageId)

        val update = """{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},"chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start","entities":[{"offset":0,"length":6,"type":"bot_command"}]}}"""
        val parsedUpdate = bot.update.parseUpdate(update)
        assertEquals(53192527, parsedUpdate?.updateId)
        assertEquals(10831, parsedUpdate?.message?.messageId)
    }

    @Test
    fun `listener workflow`(): Unit = runBlocking {
        bot.httpClient = HttpClient(MockEngine {
            respond(
                content = ByteReadChannel(updates),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })

        var update: Update? = null


        bot.update.setListener {
            update = it
            stopListener()
        }

        assertNotNull(update)
        assertEquals(53192527, update?.updateId)
        assertEquals("username", update?.message?.from?.username)
        assertEquals("John Doe", update?.message?.from?.firstName)
    }

    companion object {
        val updates = """
            {"ok":true,"result":[{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},"chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start","entities":[{"offset":0,"length":6,"type":"bot_command"}]}}]}
        """.trimIndent()
    }

}
