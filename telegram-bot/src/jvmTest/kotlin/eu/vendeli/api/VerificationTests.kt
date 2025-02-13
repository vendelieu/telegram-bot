@file:Suppress("ktlint:standard:function-naming")

package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.verification.removeChatVerification
import eu.vendeli.tgbot.api.verification.removeUserVerification
import eu.vendeli.tgbot.api.verification.verifyChat
import eu.vendeli.tgbot.api.verification.verifyUser

class VerificationTests : BotTestContext() {
    @Test
    suspend fun `verify user test`() {
        verifyUser(TG_ID) {
            "test"
        }.sendReturning(bot).shouldFailure() shouldContainInDescription "BOT_VERIFIER_FORBIDDEN"
    }

    @Test
    suspend fun `verify chat test`() {
        verifyChat(CHAT_ID) {
            "test"
        }.sendReturning(bot).shouldFailure() shouldContainInDescription "BOT_VERIFIER_FORBIDDEN"
    }

    @Test
    suspend fun `remove user verification test`() {
        removeUserVerification(TG_ID)
            .sendReturning(bot)
            .shouldFailure() shouldContainInDescription "BOT_VERIFIER_FORBIDDEN"
    }

    @Test
    suspend fun `remove chat verification test`() {
        removeChatVerification(CHAT_ID)
            .sendReturning(bot)
            .shouldFailure() shouldContainInDescription "BOT_VERIFIER_FORBIDDEN"
    }
}
