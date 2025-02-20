package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.deleteWebhook
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.api.botactions.getWebhookInfo
import eu.vendeli.tgbot.api.botactions.setWebhook
import eu.vendeli.tgbot.types.component.foldResponse
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldStartWith

class BotMainActionsTest : BotTestContext() {
    @Test
    suspend fun `get updates method testing`() {
        val result = getUpdates().sendReq().shouldSuccess()

        result.shouldNotBeNull()
    }

    @Test
    suspend fun `get webhook info method testing`() {
        val result = getWebhookInfo().sendReq().shouldSuccess()

        result.shouldNotBeNull()
        result.url.shouldBeEmpty()
    }

    @Test
    suspend fun `set webhook info method testing`() {
        setWebhook("https://vendeli.eu").send(bot)
        setWebhook("https://vendeli.eu/1")
            .sendReturning(bot)
            .foldResponse({ println(result) }, { println(errorCode) })
        val result = getWebhookInfo().sendReq().shouldSuccess()

        result.shouldNotBeNull()
        result.url shouldStartWith "https://vendeli.eu"

        deleteWebhook().send(bot)
    }
}
