package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.utils.common.GET_UPDATES_ACTION
import eu.vendeli.tgbot.utils.common.TgException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic

class ConnectionTimeoutTest : BotTestContext() {
    @BeforeClass
    fun setup() {
        mockkStatic(::GET_UPDATES_ACTION)
        every { ::GET_UPDATES_ACTION.invoke() } returns updatesAction
        coEvery {
            updatesAction.sendReturning(any()).await()
        } throws HttpRequestTimeoutException("Request timeout has expired", null)
    }

    @Test
    suspend fun `long polling timeout test`() {
        shouldThrow<TgException> {
            bot.handleUpdates()
        }.run {
            message shouldBe "Connection timeout"
            cause.shouldNotBeNull()::class shouldBe HttpRequestTimeoutException::class
        }
    }
}
