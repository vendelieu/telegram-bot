package eu.vendeli.api.botactions

import BotTestContext
import PaymentProviderTestingOnlyCondition
import eu.vendeli.tgbot.api.payments.createInvoiceLink
import eu.vendeli.tgbot.types.component.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice
import io.kotest.assertions.retry
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import kotlin.time.Duration.Companion.minutes

@EnabledIf(PaymentProviderTestingOnlyCondition::class)
class CreateInvoiceLinkTest : BotTestContext() {
    @Test
    suspend fun `create invoice link method testing`() = retry(2, 1.minutes) {
        prepareTestBot()
        val result = createInvoiceLink(
            "test",
            "test1",
            Currency.AED,
            LabeledPrice("test3", 1000),
        ) {
            "test2"
        }.options {
            providerToken = PAYMENT_PROVIDER_TOKEN
        }.sendReq()
            .shouldSuccess()

        result.shouldNotBeNull()
        result shouldContain "https://t.me"
    }
}
