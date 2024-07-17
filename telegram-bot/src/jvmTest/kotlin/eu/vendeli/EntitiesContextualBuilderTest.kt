package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.message.message
import io.kotest.matchers.shouldBe

class EntitiesContextualBuilderTest : BotTestContext() {
    @Test
    suspend fun `several entity usage test`() {
        val result = message {
            "test message" - bold { " test" } - "test plus " - textLink("https://google.com") { "test2" }
        }.sendReq().shouldSuccess()

        with(result) {
            entities?.size shouldBe 2
        }
    }
}
