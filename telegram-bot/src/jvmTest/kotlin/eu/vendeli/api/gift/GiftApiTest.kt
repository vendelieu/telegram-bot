package eu.vendeli.api.gift

import BotTestContext
import eu.vendeli.tgbot.api.gift.convertGiftToStars
import eu.vendeli.tgbot.api.gift.getChatGifts
import eu.vendeli.tgbot.api.gift.getUserGifts
import eu.vendeli.tgbot.api.gift.giftPremiumSubscription
import eu.vendeli.tgbot.api.gift.transferGift
import eu.vendeli.tgbot.api.gift.upgradeGift
import io.kotest.matchers.shouldBe

class GiftApiTest : BotTestContext() {
    @Test
    suspend fun `convertGiftToStars test`()  {
        convertGiftToStars("test", "gift_id")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `upgradeGift test`()  {
        upgradeGift("test", "gift_id")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `transferGift test`()  {
        transferGift("test", "gift_id", 123456L)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `giftPremiumSubscription test`()  {
        // Using obviously invalid user and values to trigger a known error response
        giftPremiumSubscription(
            userId = 123456L,
            monthCount = 3,
            starCount = 1000,
            textParseMode = null,
            text = { "Enjoy!" },
        ).sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: user not found")
    }

    @Test
    suspend fun `getUserGifts test`()  {
        getUserGifts(123456L)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: user not found")
    }

    @Test
    suspend fun `getChatGifts test`()  {
        getChatGifts(123456L.asChat())
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: chat not found")
    }
}
