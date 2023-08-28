package eu.vendeli.api.chat

import BotTestContext
import eu.vendeli.tgbot.api.chat.getChat
import eu.vendeli.tgbot.api.chat.getChatAdministrators
import eu.vendeli.tgbot.api.chat.getChatMember
import eu.vendeli.tgbot.api.chat.getChatMemberCount
import eu.vendeli.tgbot.api.chat.getChatMenuButton
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.keyboard.MenuButton
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.test.runTest

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

        result.size shouldBe 2

        with(result.first()) {
            shouldBeTypeOf<ChatMember.Owner>()

            user.isBot shouldBe false
            customTitle.shouldBeNull()
        }
        with(result.last()) {
            shouldBeTypeOf<ChatMember.Administrator>()

            customTitle shouldBe "bot"
            user.isBot shouldBe true
        }
    }

    @Test
    suspend fun `get chat member test`() = runTest {
        val result = getChatMember(TG_ID).sendReturning(CHAT_ID, bot).shouldSuccess()

        with(result) {
            shouldBeTypeOf<ChatMember.Member>()

            user.id shouldBe TG_ID
            user.isBot shouldBe false
        }
    }

    @Test
    suspend fun `get chat member count test`() {
        val result = getChatMemberCount().sendReturning(CHAT_ID, bot).shouldSuccess()
        result shouldBe 3
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
