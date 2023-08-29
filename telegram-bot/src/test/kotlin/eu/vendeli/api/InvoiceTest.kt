package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.invoice
import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.utils.builders.InvoiceData
import io.kotest.assertions.retry
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.minutes

class InvoiceTest : BotTestContext() {
    @Test
    suspend fun `invoice method test`() = retry(1, 1.minutes) {
        prepareTestBot()
        val result = invoice(
            InvoiceData(
                "test",
                "test1",
                "test2",
                PAYMENT_PROVIDER_TOKEN,
                Currency.AED,
                listOf(LabeledPrice("test3", 1000)),
            ),
        ).sendReturning(TG_ID, bot).shouldSuccess()

        with(result.invoice) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test1"
            currency shouldBe Currency.AED
        }
    }
}
