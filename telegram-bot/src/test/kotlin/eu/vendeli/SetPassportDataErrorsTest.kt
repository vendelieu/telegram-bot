package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.setPassportDataErrors
import eu.vendeli.tgbot.types.passport.EncryptedPassportElementType
import eu.vendeli.tgbot.types.passport.PassportElementError.Selfie
import io.kotest.matchers.booleans.shouldBeTrue
import java.util.Base64
import kotlin.random.Random

class SetPassportDataErrorsTest : BotTestContext() {
    @Test
    suspend fun `set passport data errors method test`() {
        val result = setPassportDataErrors(
            TG_ID,
            listOf(
                Selfie(
                    EncryptedPassportElementType.Passport,
                    Base64.getEncoder().encodeToString(Random.nextBytes(32)),
                    "test1",
                ),
            ),
        ).sendAsync(bot).shouldSuccess()

        result.shouldBeTrue()
    }
}
