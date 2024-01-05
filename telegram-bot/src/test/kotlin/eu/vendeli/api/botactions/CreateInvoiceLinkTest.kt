package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.createInvoiceLink
import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice
import io.kotest.assertions.retry
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import kotlin.time.Duration.Companion.minutes

class CreateInvoiceLinkTest : BotTestContext() {
    @Test
    suspend fun `create invoice link method testing`() = retry(2, 1.minutes) {
        prepareTestBot()
        val result = createInvoiceLink(
            "test",
            "test1",
            PAYMENT_PROVIDER_TOKEN,
            Currency.AED,
            LabeledPrice("test3", 1000),
        ) {
            "test2"
        }.sendAsync(bot).shouldSuccess()

        result.shouldNotBeNull()
        result shouldContain "https://t.me"
    }
}
