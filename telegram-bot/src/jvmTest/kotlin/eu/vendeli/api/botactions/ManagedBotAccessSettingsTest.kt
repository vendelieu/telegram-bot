package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.getManagedBotAccessSettings
import eu.vendeli.tgbot.api.botactions.setManagedBotAccessSettings
import eu.vendeli.tgbot.types.component.Response
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

class ManagedBotAccessSettingsTest : BotTestContext() {
    @Test
    fun `getManagedBotAccessSettings encodes user_id`() {
        getManagedBotAccessSettings(userId = 100L).apply {
            parameters["user_id"]?.jsonPrimitive?.long shouldBe 100L
        }
    }

    @Test
    fun `setManagedBotAccessSettings encodes parameters`() {
        setManagedBotAccessSettings(
            userId = 200L,
            isAccessRestricted = true,
            addedUserIds = listOf(1L, 2L, 3L),
        ).apply {
            parameters["user_id"]?.jsonPrimitive?.long shouldBe 200L
            parameters["is_access_restricted"]?.jsonPrimitive?.boolean shouldBe true
            parameters["added_user_ids"]?.jsonArray?.map { it.jsonPrimitive.int } shouldBe listOf(1, 2, 3)
        }

        setManagedBotAccessSettings(
            userId = 200L,
            isAccessRestricted = false,
        ).apply {
            parameters.containsKey("added_user_ids") shouldBe false
            parameters["is_access_restricted"]?.jsonPrimitive?.boolean shouldBe false
        }
    }

    @Test
    suspend fun `getManagedBotAccessSettings fails for unknown user`() {
        val result = getManagedBotAccessSettings(userId = 1L).sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()
    }

    @Test
    suspend fun `setManagedBotAccessSettings fails for unknown user`() {
        val result = setManagedBotAccessSettings(userId = 1L, isAccessRestricted = false).sendReq()

        result.ok.shouldBeFalse()
        result.shouldBeInstanceOf<Response.Failure>()
    }
}
