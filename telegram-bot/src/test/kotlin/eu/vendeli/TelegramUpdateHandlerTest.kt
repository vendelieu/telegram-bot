package eu.vendeli

import eu.vendeli.tgbot.TelegramBot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
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
        val updates = """
            {"ok":true,"result":[{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},"chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start","entities":[{"offset":0,"length":6,"type":"bot_command"}]}}]}
        """.trimIndent()
        val parsedUpdates = bot.update.parseUpdates(updates)
        Assertions.assertEquals(53192527, parsedUpdates?.first()?.updateId)
        Assertions.assertEquals(10831, parsedUpdates?.first()?.message?.messageId)

        val update = """{"update_id":53192527,
            "message":{"message_id":10831,"from":{"id":1,"is_bot":false,"first_name":"John Doe","username":"username","language_code":"en"},"chat":{"id":1,"first_name":"John Doe","username":"username","type":"private"},"date":1656908266,"text":"/start","entities":[{"offset":0,"length":6,"type":"bot_command"}]}}"""
        val parsedUpdate = bot.update.parseUpdate(update)
        Assertions.assertEquals(53192527, parsedUpdate?.updateId)
        Assertions.assertEquals(10831, parsedUpdate?.message?.messageId)
    }
}
