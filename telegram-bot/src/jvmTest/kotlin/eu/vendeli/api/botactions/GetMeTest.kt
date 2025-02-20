package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.types.component.getOrNull
import eu.vendeli.tgbot.types.component.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class GetMeTest : BotTestContext() {
    @Test
    suspend fun `getme method testing`() {
        val result = getMe().sendReq()
        result.ok.shouldBeTrue()
        result.isSuccess().shouldBeTrue()
        result.getOrNull().shouldNotBeNull()
        result.getOrNull()?.isBot ?: false.shouldBeTrue()
        result.getOrNull()?.id shouldBe BOT_ID
    }
}
