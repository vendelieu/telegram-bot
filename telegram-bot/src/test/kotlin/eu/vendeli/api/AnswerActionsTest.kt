package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.answerCallbackQuery
import eu.vendeli.tgbot.api.answerInlineQuery
import eu.vendeli.tgbot.api.answerPreCheckoutQuery
import eu.vendeli.tgbot.api.answerShippingQuery
import eu.vendeli.tgbot.api.answerWebAppQuery
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.types.payment.ShippingOption
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class AnswerActionsTest : BotTestContext() {
    private val answerPhoto = InlineQueryResult.Photo(
        "testPhoto",
        "https://test.com",
        "https://test.com",
    )

    @Test
    suspend fun `answer inline query test`() {
        val result = answerInlineQuery("test") {
            +answerPhoto
        }.sendAsync(bot).await()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()

        with(result) {
            errorCode shouldBe 400
            description shouldContain "ID is invalid"
        }
    }

    @Test
    suspend fun `answer callback query test`() {
        val result = answerCallbackQuery("test").sendAsync(DUMB_USER, bot).await()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()

        with(result) {
            errorCode shouldBe 400
            description shouldContain "ID is invalid"
        }
    }

    @Test
    suspend fun `answer pre checkout query test`() {
        val failureResult = answerPreCheckoutQuery("test", false, "test1")
            .sendAsync(bot).await()
        val successResult = answerPreCheckoutQuery("test")
            .sendAsync(bot).await()

        listOf(failureResult, successResult).forEach { result ->
            result.ok.shouldBeFalse()
            result.shouldBeInstanceOf<Response.Failure>()

            with(result) {
                errorCode shouldBe 400
                description shouldContain "ID is invalid"
            }
        }
    }

    @Test
    suspend fun `answer shipping query test`() {
        val listingResult = answerShippingQuery("test") {
            +ShippingOption("testOp", "testTitle", listOf(LabeledPrice("testLbl", 1)))
        }.sendAsync(bot).await()
        val varargResult = answerShippingQuery(
            "test",
            true,
            null,
            ShippingOption("testOp", "testTitle", listOf(LabeledPrice("testLbl", 1))),
        ).sendAsync(bot).await()
        val errorResult = answerShippingQuery("test", false, "test").sendAsync(bot).await()

        listOf(listingResult, varargResult, errorResult).forEach { result ->
            result.ok.shouldBeFalse()
            result.shouldBeInstanceOf<Response.Failure>()

            with(result) {
                errorCode shouldBe 400
                description shouldContain "ID is invalid"
            }
        }
    }

    @Test
    suspend fun `answer web app query test`() {
        val result = answerWebAppQuery("test", answerPhoto).sendAsync(bot).await()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()

        with(result) {
            errorCode shouldBe 400
            description shouldContain "ID is invalid"
        }
    }
}
