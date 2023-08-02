package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.deleteWebhook
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.api.botactions.getWebhookInfo
import eu.vendeli.tgbot.api.botactions.setWebhook
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty

class BotMainActionsTest : BotTestContext()  {
    @Test
    suspend fun `get updates method testing`() {
        val request = getUpdates().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
    }

    @Test
    suspend fun `get webhook info method testing`() {
        val request = getWebhookInfo().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
        result.url.shouldBeEmpty()
    }

    @Test
    suspend fun `set webhook info method testing`() {
        setWebhook("https://vendeli.eu").send(bot)
        val request = getWebhookInfo().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
        result.url shouldBe "https://vendeli.eu"

        deleteWebhook().send(bot)
    }
}
