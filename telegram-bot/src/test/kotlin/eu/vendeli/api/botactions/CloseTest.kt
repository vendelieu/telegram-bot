package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.close
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.internal.onFailure
import io.kotest.core.annotation.Ignored
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.time.Duration.Companion.seconds

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Ignored("should have big delay")
class CloseTest : BotTestContext() {
    @Test
    @Order(1)
    suspend fun `close method testing`() {
        val result = close().sendAsync(bot).await()
        result.onFailure { failure ->
            failure.parameters?.retryAfter?.also {
                delay(it.seconds)
            }
        }
        result.ok.shouldBeTrue()
        result.isSuccess().shouldBeTrue()
        result.getOrNull().shouldNotBeNull()
    }

    @Test
    @Order(2)
    suspend fun `getting too many requests`() {
        val result = close().sendAsync(bot).await()
        result.ok.shouldBeFalse()
        result.getOrNull().shouldBeNull()
        result.onFailure {
            it.errorCode shouldBe 429
            it.parameters?.retryAfter.shouldNotBeNull()
            ((it.parameters?.retryAfter ?: 0) > 0).shouldBeTrue()
        }
    }
}
