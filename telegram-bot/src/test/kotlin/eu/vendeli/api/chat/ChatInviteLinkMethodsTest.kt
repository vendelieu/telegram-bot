package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.chat.createChatInviteLink
import eu.vendeli.tgbot.api.chat.editChatInviteLink
import eu.vendeli.tgbot.api.chat.exportChatInviteLink
import eu.vendeli.tgbot.api.chat.revokeChatInviteLink
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

@EnabledIf(ChatTestingOnlyCondition::class)
class ChatInviteLinkMethodsTest : BotTestContext() {
    @Test
    suspend fun `create chat invite link method test`() {
        val expireUnix = CUR_INSTANT.plusMillis(10000).epochSecond
        val result = createChatInviteLink().options {
            name = "test"
            createsJoinRequest = true
            expireDate = expireUnix
        }.sendReturning(CHAT_ID, bot).shouldSuccess()

        with(result) {
            creator.id shouldBe BOT_ID
            isPrimary.shouldBeFalse()
            isRevoked.shouldBeFalse()
            name shouldBe "test"
            createsJoinRequest.shouldBeTrue()
            expireDate shouldBe expireUnix
        }
    }

    @Test
    suspend fun `edit chat invite link method test`() {
        val inviteLink = createChatInviteLink().options {
            name = "test"
            createsJoinRequest = true
        }.sendReturning(CHAT_ID, bot).shouldSuccess()

        val expireUnix = CUR_INSTANT.plusMillis(1000).epochSecond
        val result = editChatInviteLink(inviteLink.inviteLink).options {
            name = "test2"
            expireDate = expireUnix
            createsJoinRequest = false
        }.sendReturning(CHAT_ID, bot).shouldSuccess()

        with(result) {
            creator.id shouldBe BOT_ID
            isPrimary.shouldBeFalse()
            isRevoked.shouldBeFalse()
            name shouldBe "test2"
            createsJoinRequest.shouldBeFalse()
            expireDate shouldBe expireUnix
        }
    }

    @Test
    suspend fun `export chat invite link method test`() {
        val result = exportChatInviteLink().sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldNotBeBlank()
    }

    @Test
    suspend fun `revoke chat invite link method test`() {
        val inviteLink = createChatInviteLink().options {
            name = "test"
            createsJoinRequest = true
        }.sendReturning(CHAT_ID, bot).shouldSuccess()

        val result = revokeChatInviteLink(inviteLink.inviteLink).sendReturning(CHAT_ID, bot).shouldSuccess()

        with(result) {
            creator.id shouldBe BOT_ID
            isPrimary.shouldBeFalse()
            isRevoked.shouldBeTrue()
            name shouldBe "test"
            createsJoinRequest.shouldBeTrue()
        }
    }
}
