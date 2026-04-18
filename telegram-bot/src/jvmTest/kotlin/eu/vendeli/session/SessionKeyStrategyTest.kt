package eu.vendeli.session

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.chat.ChatType
import eu.vendeli.tgbot.types.common.Update
import eu.vendeli.tgbot.types.component.MessageUpdate
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.session.SessionKey
import eu.vendeli.tgbot.types.session.SessionKeyStrategy
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant

class SessionKeyStrategyTest : AnnotationSpec() {
    private fun msgUpdate(chatId: Long, userId: Long, chatType: ChatType): MessageUpdate {
        val chat = Chat(id = chatId, type = chatType)
        val msg = Message(
            messageId = 1L,
            from = User(id = userId, isBot = false, firstName = "u"),
            chat = chat,
            date = Instant.DISTANT_PAST,
            text = "hi",
        )
        return MessageUpdate(updateId = 1, origin = Update(1, msg), message = msg)
    }

    @Test
    fun chatUser_userPresent_producesChatUserKey() {
        val u = msgUpdate(chatId = -100, userId = 7, chatType = ChatType.Supergroup)
        SessionKeyStrategy.ChatUser.resolve(u) shouldBe SessionKey.ChatUser(chatId = -100, userId = 7)
    }

    @Test
    fun chat_strategyAlwaysChatWide() {
        val u = msgUpdate(chatId = 42, userId = 9, chatType = ChatType.Supergroup)
        SessionKeyStrategy.Chat.resolve(u) shouldBe SessionKey.Chat(chatId = 42)
    }

    @Test
    fun auto_privateChat_collapsesToChat() {
        val u = msgUpdate(chatId = 7, userId = 7, chatType = ChatType.Private)
        SessionKeyStrategy.Auto.resolve(u) shouldBe SessionKey.Chat(chatId = 7)
    }

    @Test
    fun auto_group_expandsToChatUser() {
        val u = msgUpdate(chatId = -500, userId = 3, chatType = ChatType.Group)
        SessionKeyStrategy.Auto.resolve(u) shouldBe SessionKey.ChatUser(chatId = -500, userId = 3)
    }
}
