package eu.vendeli.api.business

import BotTestContext
import eu.vendeli.tgbot.api.business.deleteBusinessMessages
import eu.vendeli.tgbot.api.business.getBusinessAccountGifts
import eu.vendeli.tgbot.api.business.getBusinessAccountStarBalance
import eu.vendeli.tgbot.api.business.readBusinessMessage
import eu.vendeli.tgbot.api.business.removeBusinessAccountProfilePhoto
import eu.vendeli.tgbot.api.business.setBusinessAccountBio
import eu.vendeli.tgbot.api.business.setBusinessAccountGiftSettings
import eu.vendeli.tgbot.api.business.setBusinessAccountName
import eu.vendeli.tgbot.api.business.setBusinessAccountProfilePhoto
import eu.vendeli.tgbot.api.business.setBusinessAccountUsername
import eu.vendeli.tgbot.api.business.transferBusinessAccountStars
import eu.vendeli.tgbot.types.gift.AcceptedGiftTypes
import eu.vendeli.tgbot.types.media.InputProfilePhoto
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import utils.RandomPicResource

class BusinessApiTest : BotTestContext() {
    @Test
    suspend fun `getBusinessAccountStarBalance test`() {
        getBusinessAccountStarBalance("test")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `transferBusinessAccountStars test`()  {
        transferBusinessAccountStars("test", 1)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `getBusinessAccountGifts test`()  {
        getBusinessAccountGifts("test")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `set business account profile photo test`()  {
        val image = bot.httpClient
            .get(RandomPicResource.current.getPicUrl(400, 400))
            .readRawBytes()
            .toImplicitFile("test.png", "image/png")

        setBusinessAccountProfilePhoto("test", InputProfilePhoto.Static(image), true)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `delete business messages test`()  {
        deleteBusinessMessages("test", listOf(1L, 2L))
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `read business message test`()  {
        readBusinessMessage("test", 1L)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `remove business account profile photo test`()  {
        removeBusinessAccountProfilePhoto("test")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `set business account bio test`()  {
        setBusinessAccountBio("test", "bio")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `set business account gift settings test`()  {
        setBusinessAccountGiftSettings(
            "test",
            true,
            AcceptedGiftTypes(
                unlimitedGifts = true,
                limitedGifts = true,
                uniqueGifts = true,
                giftsFromChannels = true,
                premiumSubscription = true,
            ),
        ).sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `set business account name test`()  {
        setBusinessAccountName("test", "name")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `set business account username test`()  {
        setBusinessAccountUsername("test", "username")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }
}
