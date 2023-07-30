package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.contact
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ContactTest : BotTestContext() {
    @Test
    suspend fun `location method test`() {
        val request = contact("test", "+0 0000 000 00 00").options {
            lastName = "test1"
            vcard = "test2"
        }.sendAsync(TG_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }

        with(result.contact) {
            shouldNotBeNull()
            firstName shouldBe "test"
            lastName shouldBe "test1"
            phoneNumber shouldBe "+000000000000"
            vcard shouldBe "test2"
        }
    }
}
