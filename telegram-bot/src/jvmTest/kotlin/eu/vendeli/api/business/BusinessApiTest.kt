package eu.vendeli.api.business

import BotTestContext
import eu.vendeli.tgbot.api.business.getBusinessAccountGifts
import eu.vendeli.tgbot.api.business.getBusinessAccountStarBalance
import eu.vendeli.tgbot.api.business.setBusinessAccountProfilePhoto
import eu.vendeli.tgbot.api.business.transferBusinessAccountStars
import eu.vendeli.tgbot.types.media.InputProfilePhoto
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.test.runTest
import utils.RandomPicResource

class BusinessApiTest : BotTestContext() {
    @Test
    fun `getBusinessAccountStarBalance test`() = runTest {
        getBusinessAccountStarBalance("test")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `transferBusinessAccountStars test`() = runTest {
        transferBusinessAccountStars("test", 1)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `getBusinessAccountGifts test`() = runTest {
        getBusinessAccountGifts("test")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `set business account profile photo test`() = runTest {
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
}
