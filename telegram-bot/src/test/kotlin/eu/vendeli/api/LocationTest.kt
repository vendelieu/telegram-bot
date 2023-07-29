package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.editMessageLiveLocation
import eu.vendeli.tgbot.api.location
import eu.vendeli.tgbot.api.stopMessageLiveLocation
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class LocationTest : BotTestContext() {
    @Test
    suspend fun `location method test`() {
        val request = location(1F, 2F).options {
            horizontalAccuracy = 3F
            livePeriod = 1000
            heading = 4
            proximityAlertRadius = 10
        }.sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
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
        val location = location(1F, 2F).options {
            horizontalAccuracy = 3F
            livePeriod = 1000
            heading = 4
            proximityAlertRadius = 10
        }.sendAsync(TG_ID, bot).await()

        val editReq = editMessageLiveLocation(location.getOrNull()!!.messageId, 2F, 3F).options {
            heading = 3
            horizontalAccuracy = 2F
            proximityAlertRadius = 9
        }.sendAsync(TG_ID, bot).await()

        val result = with(editReq) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
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
        val location = location(1F, 2F).options {
            horizontalAccuracy = 3F
            livePeriod = 1000
            heading = 4
            proximityAlertRadius = 10
        }.sendAsync(TG_ID, bot).await()

        val request = stopMessageLiveLocation(location.getOrNull()!!.messageId).sendAsync(TG_ID, bot).await()
        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        with(result.location) {
            shouldNotBeNull()
            latitude shouldBe 1.000002F
            longitude shouldBe 1.999996F
            horizontalAccuracy shouldBe 3.000000F
        }
    }
}
