package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.getUserChatBoosts
import eu.vendeli.tgbot.api.getUserProfilePhotos
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull

class UserTest : BotTestContext() {
    @Test
    suspend fun `get user profile photos method test`() {
        val idResult = getUserProfilePhotos(TG_ID, 0, 1).sendReq().shouldSuccess()
        val userResult = getUserProfilePhotos(TG_ID.asUser(), 1, 1)
            .sendReq()
            .shouldSuccess()

        listOf(idResult, userResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                totalCount shouldBeGreaterThan 1
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
