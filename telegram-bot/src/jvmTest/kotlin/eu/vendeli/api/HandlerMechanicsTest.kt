package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.implementations.DefaultFilter
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.utils.MockUpdate
import io.kotest.matchers.booleans.shouldBeTrue

class HandlerMechanicsTest : BotTestContext() {
    @Test
    suspend fun `check default guard test`() {
        DefaultGuard.condition(null, MockUpdate.SINGLE().updates.first(), bot).shouldBeTrue()
    }

    @Test
    suspend fun `check default filter test`() {
        DefaultFilter.match(null, MockUpdate.SINGLE().updates.first(), bot).shouldBeTrue()
    }
}
