package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.chat.createChatInviteLink
import eu.vendeli.tgbot.api.chat.createChatSubscriptionInviteLink
import eu.vendeli.tgbot.api.chat.editChatInviteLink
import eu.vendeli.tgbot.api.chat.editChatSubscriptionInviteLink
import eu.vendeli.tgbot.api.chat.exportChatInviteLink
import eu.vendeli.tgbot.api.chat.revokeChatInviteLink
import eu.vendeli.tgbot.types.internal.getOrNull
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@EnabledIf(ChatTestingOnlyCondition::class)
class ChatInviteLinkMethodsTest : BotTestContext() {
    @Test
    suspend fun `create chat invite link method test`() {
        val expireUnix = CUR_INSTANT.plus(120.seconds)
        val result = createChatInviteLink()
            .options {
                name = "test"
                createsJoinRequest = true
                expireDate = expireUnix
            }.sendReturning(CHAT_ID, bot)
            .shouldSuccess()

        with(result) {
            creator.id shouldBe BOT_ID
            isPrimary.shouldBeFalse()
            isRevoked.shouldBeFalse()
            name shouldBe "test"
            createsJoinRequest.shouldBeTrue()
            expireDate?.epochSeconds shouldBe expireUnix.epochSeconds
        }
    }

    @Test
    suspend fun `create chat subscription invite link method test`() {
        val expireUnix = CUR_INSTANT.plus(30.days)
        createChatSubscriptionInviteLink(10, "test")
            .sendReturning(CHAT_ID, bot)
            .getOrNull()?.run {
                creator.id shouldBe BOT_ID
                isPrimary.shouldBeFalse()
                isRevoked.shouldBeFalse()
                name shouldBe "test"
                createsJoinRequest.shouldBeTrue()
                expireDate?.epochSeconds shouldBe expireUnix.epochSeconds
            }
    }

    @Test
    suspend fun `edit chat subscription invite link method test`() {
        editChatSubscriptionInviteLink("test", "test2")
            .sendReturning(CHAT_ID, bot)
            .getOrNull()?.run {
                creator.id shouldBe BOT_ID
                isPrimary.shouldBeFalse()
                isRevoked.shouldBeFalse()
                name shouldBe "test2"
                createsJoinRequest.shouldBeTrue()
            }
    }
    @Test
    suspend fun `edit chat invite link method test`() {
        val inviteLink = createChatInviteLink()
            .options {
                name = "test"
                createsJoinRequest = true
            }.sendReturning(CHAT_ID, bot)
            .shouldSuccess()

        val expireUnix = CUR_INSTANT.plus(1000.milliseconds)
        val result = editChatInviteLink(inviteLink.inviteLink)
            .options {
                name = "test2"
                expireDate = expireUnix
                createsJoinRequest = false
            }.sendReturning(CHAT_ID, bot)
            .shouldSuccess()

        with(result) {
            creator.id shouldBe BOT_ID
            isPrimary.shouldBeFalse()
            isRevoked.shouldBeFalse()
            name shouldBe "test2"
            createsJoinRequest.shouldBeFalse()
            expireDate?.epochSeconds shouldBe expireUnix.epochSeconds
        }
    }

    @Test
    suspend fun `export chat invite link method test`() {
        val result = exportChatInviteLink().sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldNotBeBlank()
    }

    @Test
    suspend fun `revoke chat invite link method test`() {
        val inviteLink = createChatInviteLink()
            .options {
                name = "test"
                createsJoinRequest = true
            }.sendReturning(CHAT_ID, bot)
            .shouldSuccess()

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
