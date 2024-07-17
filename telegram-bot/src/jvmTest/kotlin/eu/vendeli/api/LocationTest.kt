package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.location
import eu.vendeli.tgbot.api.message.editMessageLiveLocation
import eu.vendeli.tgbot.api.stopMessageLiveLocation
import eu.vendeli.tgbot.api.venue
import eu.vendeli.tgbot.types.internal.getOrNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class LocationTest : BotTestContext() {
    @Test
    suspend fun `location method test`() {
        val result = location(1F, 2F)
            .options {
                horizontalAccuracy = 3F
                livePeriod = 1000
                heading = 4
                proximityAlertRadius = 10
            }.sendReq()
            .shouldSuccess()

        with(result.location) {
            shouldNotBeNull()
            latitude shouldBe 1.000002F
            longitude shouldBe 1.999996F
            horizontalAccuracy shouldBe 3.000000F
            livePeriod shouldBe 1000
            heading shouldBe 4
            proximityAlertRadius shouldBe 10
        }
    }

    @Test
    suspend fun `edit live location method test`() {
        val location = location(1F, 2F)
            .options {
                horizontalAccuracy = 3F
                livePeriod = 1000
                heading = 4
                proximityAlertRadius = 10
            }.sendReq()

        val result = editMessageLiveLocation(location.getOrNull()!!.messageId, 2F, 3F)
            .options {
                heading = 3
                horizontalAccuracy = 2F
                proximityAlertRadius = 9
            }.sendReq()
            .shouldSuccess()

        with(result.location) {
            shouldNotBeNull()
            latitude shouldBe 2.000004F
            longitude shouldBe 2.999999F
            horizontalAccuracy shouldBe 2.000000F
            livePeriod shouldBe 1000
            heading shouldBe 3
            proximityAlertRadius shouldBe 9
        }
    }

    @Test
    suspend fun `stop live location method test`() {
        val location = location(1F, 2F)
            .options {
                horizontalAccuracy = 3F
                livePeriod = 1000
                heading = 4
                proximityAlertRadius = 10
            }.sendReq()

        val result = stopMessageLiveLocation(
            location.getOrNull()!!.messageId,
        ).sendReq().shouldSuccess()

        with(result.location) {
            shouldNotBeNull()
            latitude shouldBe 1.000002F
            longitude shouldBe 1.999996F
            horizontalAccuracy shouldBe 3.000000F
        }
    }

    @Test
    suspend fun `venue location method test`() {
        val result = venue(
            1F,
            2F,
            "test",
            "address",
        ).sendReq().shouldSuccess()

        with(result) {
            venue.shouldNotBeNull()
            venue?.title shouldBe "test"
            venue?.address shouldBe "address"

            location?.latitude shouldBe 1f
            location?.longitude shouldBe 1.999999F
        }
    }
}
