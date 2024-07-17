package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getStarTransactions
import io.kotest.matchers.shouldBe

class GetStarTransactionsTest : BotTestContext() {
    @Test
    suspend fun `getStarTransactions method testing`() {
        val result = getStarTransactions(10, 20).sendReq().shouldSuccess()

        result.run {
            transactions.size shouldBe 0
        }
    }
}
