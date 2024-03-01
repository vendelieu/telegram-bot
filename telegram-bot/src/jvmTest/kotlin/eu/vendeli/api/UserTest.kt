package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.getUserChatBoosts
import eu.vendeli.tgbot.api.getUserProfilePhotos
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class UserTest : BotTestContext() {
    @Test
    suspend fun `get user profile photos method test`() {
        val idResult = getUserProfilePhotos(TG_ID, 0, 1).sendAsync(bot).shouldSuccess()
        val userResult = getUserProfilePhotos(TG_ID.asUser(), 1, 1)
            .sendAsync(bot).shouldSuccess()

        listOf(idResult, userResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                totalCount shouldBe 1
                photos.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `get user chat boost method test`() {
        getUserChatBoosts(TG_ID.asUser()).sendReturning(CHAT_ID, bot).shouldSuccess().run {
            boosts.shouldBeEmpty()
        }
    }
}
