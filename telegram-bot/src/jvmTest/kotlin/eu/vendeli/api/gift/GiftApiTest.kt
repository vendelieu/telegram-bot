package eu.vendeli.api.gift

import BotTestContext
import eu.vendeli.tgbot.api.gift.convertGiftToStars
import eu.vendeli.tgbot.api.gift.giftPremiumSubscription
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

    @Test
    fun `giftPremiumSubscription test`() = runTest {
        // Using obviously invalid user and values to trigger a known error response
        giftPremiumSubscription(
            userId = 123456L,
            monthCount = 3,
            starCount = 1000,
            textParseMode = null,
            text = { "Enjoy!" }
        ).sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: user not found")
    }
}
