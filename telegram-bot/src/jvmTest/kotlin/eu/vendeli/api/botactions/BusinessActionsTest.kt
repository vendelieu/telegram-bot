package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getBusinessConnection
import eu.vendeli.tgbot.types.component.getOrNull
import io.kotest.matchers.shouldBe

class BusinessActionsTest : BotTestContext() {
    @Test
    suspend fun `getBusinessConnection method testing`() {
        getBusinessConnection("test")
            .sendReq()
            .getOrNull()
            ?.user
            ?.id
            ?.shouldBe(TG_ID)
    }
}
