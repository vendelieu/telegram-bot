package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.passport.setPassportDataErrors
import eu.vendeli.tgbot.types.passport.EncryptedPassportElementType
import eu.vendeli.tgbot.types.passport.PassportElementError.Selfie
import io.kotest.matchers.booleans.shouldBeTrue

class SetPassportDataErrorsTest : BotTestContext() {
    @Test
    suspend fun `set passport data errors method test`() {
        val result = setPassportDataErrors(
            TG_ID,
            listOf(
                Selfie(
                    EncryptedPassportElementType.Passport,
                    "rvrAIw/dzwLEBykk4guKZw3P+Ld/2RtkyZuVDGsJEqQ=",
                    "test1",
                ),
            ),
        ).sendReq().shouldSuccess()

        result.shouldBeTrue()
    }
}
