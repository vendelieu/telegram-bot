package eu.vendeli.api.gift

import BotTestContext
import eu.vendeli.tgbot.api.gift.convertGiftToStars
import eu.vendeli.tgbot.api.gift.transferGift
import eu.vendeli.tgbot.api.gift.upgradeGift
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class GiftApiTest : BotTestContext() {
    @Test
    fun `convertGiftToStars test`() = runTest {
        convertGiftToStars("test", "gift_id")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `upgradeGift test`() = runTest {
        upgradeGift("test", "gift_id")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `transferGift test`() = runTest {
        transferGift("test", "gift_id", 123456L)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }
}
