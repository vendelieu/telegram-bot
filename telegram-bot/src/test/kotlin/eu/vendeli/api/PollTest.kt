package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.poll
import eu.vendeli.tgbot.api.stopPoll
import eu.vendeli.tgbot.types.PollOption
import eu.vendeli.tgbot.types.PollType
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class PollTest : BotTestContext() {
    @Test
    suspend fun `poll method test`() {
        listOf(
            poll("Test", "test1", "test2"),
            poll("Test") { arrayOf("test1", "test2") },
        ).forEach { action ->
            val request = action.options {
                type = PollType.Quiz
                openPeriod = 565
                correctOptionId = 1
                isAnonymous = false
            }.sendAsync(TG_ID, bot).await()

            val result = with(request) {
                ok.shouldBeTrue()
                isSuccess().shouldBeTrue()
                getOrNull().shouldNotBeNull()
            }

            with(result.poll) {
                shouldNotBeNull()
                question shouldBe "Test"
                isAnonymous shouldBe false
                options shouldContainExactly listOf(PollOption("test1", 0), PollOption("test2", 0))
                openPeriod?.toLong() shouldBe 565
                type shouldBe PollType.Quiz
                correctOptionId shouldBe 1
            }
        }
    }

    @Test
    suspend fun `close poll method test`() {
        val poll = poll("Test", "test1", "test2").options {
            type = PollType.Quiz
            openPeriod = 565
            correctOptionId = 1
            isAnonymous = false
        }.sendAsync(TG_ID, bot).await()

        poll.getOrNull().shouldNotBeNull().poll.shouldNotBeNull().run {
            isClosed shouldBe false
        }

        stopPoll(
            poll.getOrNull()!!.messageId,
        ).sendAsync(TG_ID, bot).await().getOrNull().shouldNotBeNull().run {
            isClosed shouldBe true
        }
    }
}
