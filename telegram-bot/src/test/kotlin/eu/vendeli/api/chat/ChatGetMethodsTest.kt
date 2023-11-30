package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.chat.getChat
import eu.vendeli.tgbot.api.chat.getChatAdministrators
import eu.vendeli.tgbot.api.chat.getChatMember
import eu.vendeli.tgbot.api.chat.getChatMemberCount
import eu.vendeli.tgbot.api.chat.getChatMenuButton
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.keyboard.MenuButton
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

@EnabledIf(ChatTestingOnlyCondition::class)
class ChatGetMethodsTest : BotTestContext() {
    @Test
    suspend fun `get chat method test`() {
        val result = getChat().sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            id shouldBe TG_ID
            type shouldBe ChatType.Private
        }
    }

    @Test
    suspend fun `get chat administrators test`() {
        val result = getChatAdministrators().sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldNotBeEmpty()
    }

    @Test
    suspend fun `get chat member test`() {
        val result = getChatMember(TG_ID).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.status.shouldNotBeNull()
    }

    @Test
    suspend fun `get chat member count test`() {
        val result = getChatMemberCount().sendReturning(CHAT_ID, bot).shouldSuccess()
        result shouldBe 4
    }

    @Test
    suspend fun `get chat menu button test`() {
        val result = getChatMenuButton().sendReturning(TG_ID, bot).shouldSuccess()

        with(result) {
            shouldBeTypeOf<MenuButton.Default>()
            type shouldBe "default"
        }
    }
}
