package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.poll
import eu.vendeli.tgbot.api.stopPoll
import eu.vendeli.tgbot.types.msg.EntityType
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.poll.InputPollOption
import eu.vendeli.tgbot.types.poll.PollOption
import eu.vendeli.tgbot.types.poll.PollType
import eu.vendeli.tgbot.utils.serde
import eu.vendeli.tgbot.utils.toJsonElement
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class PollTest : BotTestContext() {
    @Test
    suspend fun `poll method test`() {
        listOf(
            poll("Test", InputPollOption("test1"), InputPollOption("test2")),
            poll("Test") {
                option { "test1" }
                option { "test2" }
            },
        ).forEach { action ->
            val result = action
                .options {
                    type = PollType.Quiz
                    openPeriod = 2.minutes
                    correctOptionId = 1
                    isAnonymous = false
                }.sendReq()
                .shouldSuccess()

            with(result.poll) {
                shouldNotBeNull()
                question shouldBe "Test"
                isAnonymous shouldBe false
                options shouldContainExactly listOf(PollOption("test1", 0), PollOption("test2", 0))
                openPeriod?.toLong() shouldBe 120.seconds.inWholeSeconds
                type shouldBe PollType.Quiz
                correctOptionId shouldBe 1
            }
        }
    }

    @Test
    suspend fun `close poll method test`() {
        val poll = poll("Test", InputPollOption("test1"), InputPollOption("test1"))
            .options {
                type = PollType.Quiz
                openPeriod = 565.seconds
                correctOptionId = 1
                isAnonymous = false
            }.sendReq()

        poll.getOrNull().shouldNotBeNull().poll.shouldNotBeNull().run {
            isClosed shouldBe false
        }

        stopPoll(
            poll.getOrNull()!!.messageId,
        ).sendReq().getOrNull().shouldNotBeNull().run {
            isClosed shouldBe true
        }
    }

    @Test
    fun `poll option builder test`() {
        poll("test") {
            option { "test".customEmoji("1234") + " test2" }
        }.apply {
            parameters.size shouldBe 2
            parameters["question"] shouldBe "test".toJsonElement()
            val options: List<Map<String, JsonElement>> = serde.decodeFromJsonElement(parameters["options"]!!)
            options.shouldHaveSize(1)
            options.first().shouldHaveSize(2)
            val inputPollOption = options.first().entries
            inputPollOption.first().value shouldBe "test test2".toJsonElement()
            inputPollOption.last().value shouldBe serde.encodeToJsonElement(
                listOf(
                    MessageEntity(
                        EntityType.CustomEmoji,
                        4,
                        2,
                        customEmojiId = "1234",
                    ),
                ),
            )
        }
    }
}
