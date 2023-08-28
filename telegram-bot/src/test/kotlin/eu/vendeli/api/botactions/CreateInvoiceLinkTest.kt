package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.createInvoiceLink
import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.utils.builders.InvoiceData
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain

class CreateInvoiceLinkTest : BotTestContext() {

    @Test
    suspend fun `create invoice link method testing`() {
        val result = createInvoiceLink(
            InvoiceData(
                "test",
                "test1",
                "test2",
                "5322214758:TEST:5c6bcca1-7ad9-44ee-a841-c288ae93aaec",
                Currency.AED,
                listOf(LabeledPrice("test3", 1000)),
            ),
        ).sendAsync(bot).await().shouldSuccess()

        result.shouldNotBeNull()
        result shouldContain "https://t.me"
    }
}
