package eu.vendeli.api.botactions

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetMeTest {
    private lateinit var bot: TelegramBot

    @BeforeAll
    fun prepareTestBot() {
        bot = TelegramBot(System.getenv("BOT_TOKEN"), "eu.vendeli")
    }

    @Test
    fun `close method testing`(): Unit = runBlocking {
        val result = getMe().sendAsync(bot).await()
        assertTrue(result.ok)
        assertTrue(result.isSuccess())
        assertNotNull(result.getOrNull())
        assertTrue(result.getOrNull()?.isBot ?: false)
        assertEquals(System.getenv("BOT_ID").toLong(), result.getOrNull()?.id)
    }
}
