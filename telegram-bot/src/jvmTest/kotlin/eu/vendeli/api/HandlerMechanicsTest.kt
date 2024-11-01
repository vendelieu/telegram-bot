package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.interfaces.helper.Guard
import eu.vendeli.tgbot.utils.fqName
import eu.vendeli.utils.MockUpdate
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class HandlerMechanicsTest : BotTestContext() {
    @Test
    suspend fun `check default guard test`() {
        DefaultGuard.condition(null, MockUpdate.SINGLE().updates.first(), bot).shouldBeTrue()
    }

    @Test
    suspend fun `check default filter test`() {
        DefaultFilter.match(null, MockUpdate.SINGLE().updates.first(), bot).shouldBeTrue()
    }

    @Test
    fun `guard name test`() {
        DefaultGuard::class.fqName shouldBe DefaultGuard::class.fqName
        Guard { _, _, _ -> true }::class.fqName shouldBe "Unknown"
    }
}
