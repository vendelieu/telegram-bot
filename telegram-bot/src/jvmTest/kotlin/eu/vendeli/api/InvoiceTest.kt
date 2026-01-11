package eu.vendeli.api

import BotTestContext
import PaymentProviderTestingOnlyCondition
import eu.vendeli.tgbot.api.payments.invoice
import eu.vendeli.tgbot.types.component.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice
import io.kotest.assertions.retry
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.minutes

@EnabledIf(PaymentProviderTestingOnlyCondition::class)
class InvoiceTest : BotTestContext() {
    @Test
    suspend fun `invoice method test`() = retry(2, 1.minutes) {
        prepareTestBot()
        val result = invoice(
            "test",
            "test1",
            "test2",
            PAYMENT_PROVIDER_TOKEN,
            Currency.USD,
            listOf(LabeledPrice("test3", 1000)),
        ).sendReq().shouldSuccess()

        with(result.invoice) {
            shouldNotBeNull()
            title shouldBe "test"
            description shouldBe "test1"
            currency shouldBe Currency.USD
        }
    }
}
