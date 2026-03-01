package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.msg.EntityType
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.common.serde
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.ListSerializer
import kotlin.time.Instant

class EntitiesContextualBuilderTest : BotTestContext() {
    @Test
    suspend fun `several entity usage test`() {
        val result = message {
            "test message" - bold { " test" } - "test plus " - textLink("https://google.com") { "test2" }
        }.sendReq().shouldSuccess()

        with(result) {
            entities?.size shouldBe 2
        }
    }

    @Test
    fun `instant to format infix adds DateTime entity`() {
        val instant = Instant.fromEpochSeconds(1735689600) // 2025-01-01 00:00:00 UTC
        val format = "dd.MM.yyyy HH:mm"

        val action = message {
            "Event at " - (instant to format)
        }

        action.parameters["text"]?.toString() shouldBe "\"Event at $format\""
        val entities = serde.decodeFromJsonElement(
            ListSerializer(MessageEntity.serializer()),
            action.parameters["entities"].shouldNotBeNull(),
        )
        entities.shouldHaveSize(1)
        with(entities.first()) {
            type shouldBe EntityType.DateTime
            unixTime.shouldNotBeNull() shouldBe instant
            dateTimeFormat shouldBe format
            offset shouldBe 9 // "Event at ".length
            length shouldBe format.length
        }
    }

    @Test
    fun `dateTime builder with two args adds DateTime entity`() {
        val instant = Instant.fromEpochSeconds(1735689600)
        val format = "yyyy-MM-dd"

        val action = message {
            "Date: " - dateTime(instant, format)
        }

        action.parameters["text"]?.toString() shouldBe "\"Date: $format\""
        val entities = serde.decodeFromJsonElement(
            ListSerializer(MessageEntity.serializer()),
            action.parameters["entities"].shouldNotBeNull(),
        )
        entities.shouldHaveSize(1)
        with(entities.first()) {
            type shouldBe EntityType.DateTime
            unixTime.shouldNotBeNull() shouldBe instant
            dateTimeFormat shouldBe format
        }
    }

    @Test
    fun `instant to format at start of message`() {
        val instant = Instant.fromEpochSeconds(1704067200) // 2024-01-01 00:00:00 UTC
        val format = "HH:mm"

        val action = message {
            (instant to format) - " (UTC)"
        }

        action.parameters["text"]?.toString() shouldBe "\"$format (UTC)\""
        val entities = serde.decodeFromJsonElement(
            ListSerializer(MessageEntity.serializer()),
            action.parameters["entities"].shouldNotBeNull(),
        )
        entities.shouldHaveSize(1)
        with(entities.first()) {
            type shouldBe EntityType.DateTime
            offset shouldBe 0
            length shouldBe format.length
        }
    }
}
