package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.answer.answerCallbackQuery
import eu.vendeli.tgbot.api.answer.answerInlineQuery
import eu.vendeli.tgbot.api.answer.answerPreCheckoutQuery
import eu.vendeli.tgbot.api.answer.answerShippingQuery
import eu.vendeli.tgbot.api.answer.answerWebAppQuery
import eu.vendeli.tgbot.types.inline.InlineQueryResult
import eu.vendeli.tgbot.types.component.Response
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
        }.sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()

        with(result) {
            errorCode shouldBe 400
            description shouldContain "ID is invalid"
        }
    }

    @Test
    suspend fun `answer callback query test`() {
        val result = answerCallbackQuery("test").sendReq()

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
            .sendReq()
        val successResult = answerPreCheckoutQuery("test")
            .sendReq()

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
        }.sendReq()
        val varargResult = answerShippingQuery(
            "test",
            true,
            null,
            ShippingOption("testOp", "testTitle", listOf(LabeledPrice("testLbl", 1))),
        ).sendReq()
        val errorResult = answerShippingQuery("test", false, "test").sendReq()

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
        val result = answerWebAppQuery("test", answerPhoto).sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()

        with(result) {
            errorCode shouldBe 400
            description shouldContain "ID is invalid"
        }
    }
}
