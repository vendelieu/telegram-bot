package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.deleteWebhook
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.api.botactions.getWebhookInfo
import eu.vendeli.tgbot.api.botactions.setWebhook
import eu.vendeli.tgbot.types.internal.foldResponse
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty

class BotMainActionsTest : BotTestContext() {
    @Test
    suspend fun `get updates method testing`() {
        val result = getUpdates().sendAsync(bot).await().shouldSuccess()

        result.shouldNotBeNull()
    }

    @Test
    suspend fun `get webhook info method testing`() {
        val result = getWebhookInfo().sendAsync(bot).await().shouldSuccess()

        result.shouldNotBeNull()
        result.url.shouldBeEmpty()
    }

    @Test
    suspend fun `set webhook info method testing`() {
        setWebhook("https://vendeli.eu").send(bot)
        setWebhook("https://vendeli.eu/1").sendAsync(bot)
            .foldResponse({ println(result) }, { println(errorCode) })
        val result = getWebhookInfo().sendAsync(bot).await().shouldSuccess()

        result.shouldNotBeNull()
        result.url shouldBe "https://vendeli.eu"

        deleteWebhook().send(bot)
    }
}
