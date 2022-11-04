package eu.vendeli.api.botactions

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.botactions.close
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.internal.onFailure
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CloseTest {
    private lateinit var bot: TelegramBot

    @BeforeAll
    fun prepareTestBot() {
        bot = TelegramBot.Builder(System.getenv("BOT_TOKEN")) {
            controllersPackage = "eu.vendeli"
        }.build()
    }

    @Test
    @Order(1)
    fun `close method testing`(): Unit = runBlocking {
        val result = close().sendAsync(bot).await()
        assertTrue(result.ok)
        assertTrue(result.isSuccess())
        assertNotNull(result.getOrNull())
    }

    @Test
    @Order(2)
    fun `getting too many requests`(): Unit = runBlocking {
        val result = close().sendAsync(bot).await()
        assertFalse(result.ok)
        assertNull(result.getOrNull())
        result.onFailure {
            assertEquals(429, it.errorCode)
            assertNotNull(it.parameters?.retryAfter)
            assertTrue((it.parameters?.retryAfter ?: 0) > 0)
        }
    }
}
