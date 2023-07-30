package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.getUserProfilePhotos
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class GetUserProfilePhotosTest : BotTestContext() {
    @Test
    suspend fun `get user profile photos method test`() {
        val request = getUserProfilePhotos(TG_ID, 0, 1).sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result) {
            shouldNotBeNull()
            totalCount shouldBe 1
            photos.shouldNotBeNull()
        }
    }
}
