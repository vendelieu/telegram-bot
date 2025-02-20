package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.game.game
import eu.vendeli.tgbot.api.game.getGameHighScores
import eu.vendeli.tgbot.api.game.setGameScore
import eu.vendeli.tgbot.types.component.getOrNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.random.Random
import kotlin.random.nextLong

class GameTest : BotTestContext() {
    @Test
    suspend fun `send game method test`() {
        val result = game("testestes").sendReq().shouldSuccess()

        with(result.game) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test2"
        }
    }

    @Test
    suspend fun `set score method test`() {
        val game = game("testestes").sendReq().getOrNull()

        val userResult = setGameScore(TG_ID.asUser(), game!!.messageId, ITER_INT.toLong())
            .options {
                force = true
            }.sendReq()
            .shouldSuccess()
        val idResult = setGameScore(TG_ID, game.messageId, ITER_INT.toLong())
            .options {
                force = true
            }.sendReq()
            .shouldSuccess()

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
        val game = game("testestes").sendReq().getOrNull()
        val newScore = Random.nextLong(1L..10_000)
        setGameScore(TG_ID, game!!.messageId, newScore)
            .options {
                force = true
            }.sendReq()

        val idResult = getGameHighScores(TG_ID, game.messageId)
            .sendReq()
            .shouldSuccess()
        val userResult = getGameHighScores(TG_ID.asUser(), game.messageId)
            .sendReq()
            .shouldSuccess()

        listOf(idResult, userResult).forEach { result ->
            with(result.first()) {
                score shouldBe newScore
                position shouldBe 1
                user.id shouldBe TG_ID
            }
        }
    }
}
