package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.getUserChatBoosts
import eu.vendeli.tgbot.api.getUserProfilePhotos
import eu.vendeli.tgbot.types.internal.Response
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class UserTest : BotTestContext() {
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

    @Test
    suspend fun `get user chat boost method test`() {
        getUserChatBoosts(TG_ID.asUser()).sendReturning(CHAT_ID, bot).run {
            ok.shouldBeFalse()
            shouldBeInstanceOf<Response.Failure>().description shouldContain "PEER_ID_INVALID"
        }
    }
}
