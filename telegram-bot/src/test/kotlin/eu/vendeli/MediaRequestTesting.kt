package eu.vendeli

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.media.photo
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.ParseMode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MediaRequestTesting {
    private lateinit var bot: TelegramBot
    private var classloader = Thread.currentThread().contextClassLoader

    @BeforeAll
    fun prepareTestBot() {
        bot = TelegramBot.Builder(System.getenv("BOT_TOKEN")) {
            controllersPackage = "eu.vendeli"
        }.build()
    }

    @Test
    fun `media requests testing`(): Unit = runBlocking {
        val image = classloader.getResource("image.png")?.readBytes() ?: ByteArray(0)

        photo(image).options { parseMode = ParseMode.HTML }.caption { "\"test\"" }
            .sendAsync(System.getenv("TELEGRAM_ID").toLong(), bot).await()
    }
}
