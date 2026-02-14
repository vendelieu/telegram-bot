package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.chat.getUserChatBoosts
import eu.vendeli.tgbot.api.media.getUserProfileAudios
import eu.vendeli.tgbot.api.media.getUserProfilePhotos
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
    suspend fun `get user profile audios method test`() {
        val idResult = getUserProfileAudios(TG_ID, 0, 10).sendReq().shouldSuccess()
        val userResult = getUserProfileAudios(TG_ID.asUser(), 0, 10).sendReq().shouldSuccess()

        listOf(idResult, userResult).forEach { result ->
            with(result) {
                shouldNotBeNull()
                audios.shouldNotBeNull()
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
