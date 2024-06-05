package eu.vendeli

import BotTestContext
import eu.vendeli.tgbot.api.message.editText
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.EntityType
import io.kotest.assertions.throwables.shouldNotThrowAny

class ActionExtTest : BotTestContext() {
    @Test
    suspend fun `InlineExt test`() = shouldNotThrowAny {
        editText { "test" }.entities {
            entity(EntityType.Bold, 0, 5)
        }.sendInline("test", bot)
    }

    @Test
    suspend fun `BussinesExt test`() = shouldNotThrowAny {
        message { "test" }.sendBusiness(CHAT_ID, "test", bot)
    }
}
