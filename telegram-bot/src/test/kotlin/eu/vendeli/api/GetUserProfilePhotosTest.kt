package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.getUserProfilePhotos
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class GetUserProfilePhotosTest : BotTestContext() {
    @Test
    suspend fun `get user profile photos method test`() {
        val idResult = getUserProfilePhotos(TG_ID, 0, 1).sendAsync(bot).await().shouldSuccess()
        val userResult = getUserProfilePhotos(TG_ID.asUser(), 1, 1)
            .sendAsync(bot).await().shouldSuccess()

        listOf(idResult, userResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                totalCount shouldBe 1
                photos.shouldNotBeNull()
            }
        }
    }
}
