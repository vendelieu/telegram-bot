package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.game
import eu.vendeli.tgbot.api.getGameHighScores
import eu.vendeli.tgbot.api.setGameScore
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlin.random.Random
import kotlin.random.nextLong

class GameTest : BotTestContext() {
    @Test
    suspend fun `send game method test`() {
        val request = game("testestes").sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result.game) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test2"
        }
    }

    @Test
    suspend fun `set score method test`() {
        val game = game("testestes").sendAsync(TG_ID, bot).await().getOrNull()
        val newScore = ITERATION.nextInt().toLong()

        val request = setGameScore(TG_ID, game!!.messageId, newScore).options {
            force = true
        }.sendAsync(TG_ID, bot).await()
        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result.game) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test2"
            text shouldContain "$newScore"
        }
    }

    @Test
    suspend fun `get game high score method test`() {
        val game = game("testestes").sendAsync(TG_ID, bot).await().getOrNull()
        val newScore = Random.nextLong(1L..10_000)
        setGameScore(TG_ID, game!!.messageId, newScore).options {
            force = true
        }.sendAsync(TG_ID, bot).await()

        val request = getGameHighScores(TG_ID, game.messageId).sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result.first()) {
            score shouldBe newScore
            position shouldBe 1
            user.id shouldBe TG_ID
        }
    }
}
