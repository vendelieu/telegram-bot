package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.game
import eu.vendeli.tgbot.api.getGameHighScores
import eu.vendeli.tgbot.api.setGameScore
import eu.vendeli.tgbot.types.internal.getOrNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.random.Random
import kotlin.random.nextLong

class GameTest : BotTestContext() {
    @Test
    suspend fun `send game method test`() {
        val result = game("testestes").sendReturning(TG_ID, bot).shouldSuccess()

        with(result.game) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test2"
        }
    }

    @Test
    suspend fun `set score method test`() {
        val game = game("testestes").sendReturning(TG_ID, bot).getOrNull()

        val userResult = setGameScore(TG_ID.wrapToUser(), game!!.messageId, RAND_INT.toLong()).options {
            force = true
        }.sendReturning(TG_ID, bot).shouldSuccess()
        val idResult = setGameScore(TG_ID, game.messageId, RAND_INT.toLong()).options {
            force = true
        }.sendReturning(TG_ID, bot).shouldSuccess()

        listOf(userResult, idResult).forEach { result ->
            with(result.game) {
                shouldNotBeNull()
                title shouldBe "test"
                description shouldBe "test2"
            }
        }
    }

    @Test
    suspend fun `get game high score method test`() {
        val game = game("testestes").sendReturning(TG_ID, bot).getOrNull()
        val newScore = Random.nextLong(1L..10_000)
        setGameScore(TG_ID, game!!.messageId, newScore).options {
            force = true
        }.sendReturning(TG_ID, bot)

        val result = getGameHighScores(TG_ID, game.messageId).sendAsync(TG_ID, bot).await().shouldSuccess()

        with(result.first()) {
            score shouldBe newScore
            position shouldBe 1
            user.id shouldBe TG_ID
        }
    }
}
