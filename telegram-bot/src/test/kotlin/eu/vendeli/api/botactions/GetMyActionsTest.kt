package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getMyCommands
import eu.vendeli.tgbot.api.botactions.getMyDefaultAdministratorRights
import eu.vendeli.tgbot.api.botactions.getMyDescription
import eu.vendeli.tgbot.api.botactions.getMyShortDescription
import eu.vendeli.tgbot.api.botactions.getUpdates
import eu.vendeli.tgbot.api.botactions.getWebhookInfo
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldBeEmpty

class GetMyActionsTest: BotTestContext() {
    @Test
    suspend fun `get my default administrator rights method testing`() {
        val request = getMyDefaultAdministratorRights().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
    }

    @Test
    suspend fun `get description method testing`() {
        val request = getMyDescription().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
        result.description.shouldBeEmpty()
    }

    @Test
    suspend fun `get short description method testing`() {
        val request = getMyShortDescription().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
        result.shortDescription.shouldBeEmpty()
    }

    @Test
    suspend fun `get my commands method testing`() {
        val request = getMyCommands().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
        result.shouldBeEmpty()
    }

    @Test
    suspend fun `get webhook info method testing`() {
        val request = getWebhookInfo().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
        result.url.shouldBeEmpty()
    }

    @Test
    suspend fun `get updates method testing`() {
        val request = getUpdates().sendAsync(bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeNull()
    }
}
