package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.common.contact
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ContactTest : BotTestContext() {
    @Test
    suspend fun `contact method test`() {
        val result = contact("test", "+0 0000 000 00 00")
            .options {
                lastName = "test1"
                vcard = "test2"
            }.sendReq()
            .shouldSuccess()

        with(result.contact) {
            shouldNotBeNull()
            firstName shouldBe "test"
            lastName shouldBe "test1"
            phoneNumber shouldBe "+000000000000"
            vcard shouldBe "test2"
        }
    }
}
