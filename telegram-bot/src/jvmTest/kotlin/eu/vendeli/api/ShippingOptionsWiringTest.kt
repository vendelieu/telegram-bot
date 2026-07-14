package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.answer.answerShippingQuery
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.types.payment.ShippingOption
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.jsonArray

class ShippingOptionsWiringTest : BotTestContext() {
    @Test
    fun `shipping options are wired for both builder and vararg variants`() {
        val listing = answerShippingQuery("test") {
            +ShippingOption("testOp", "testTitle", listOf(LabeledPrice("testLbl", 1)))
        }
        val vararg = answerShippingQuery(
            "test",
            true,
            null,
            ShippingOption("testOp", "testTitle", listOf(LabeledPrice("testLbl", 1))),
        )

        listing.parameters["shipping_options"]?.jsonArray?.size shouldBe 1
        vararg.parameters["shipping_options"]?.jsonArray?.size shouldBe 1
    }
}
