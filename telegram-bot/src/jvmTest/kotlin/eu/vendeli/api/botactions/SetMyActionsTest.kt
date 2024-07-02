package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.deleteMyCommands
import eu.vendeli.tgbot.api.botactions.getMyCommands
import eu.vendeli.tgbot.api.botactions.getMyDefaultAdministratorRights
import eu.vendeli.tgbot.api.botactions.getMyDescription
import eu.vendeli.tgbot.api.botactions.getMyName
import eu.vendeli.tgbot.api.botactions.getMyShortDescription
import eu.vendeli.tgbot.api.botactions.setMyCommands
import eu.vendeli.tgbot.api.botactions.setMyDefaultAdministratorRights
import eu.vendeli.tgbot.api.botactions.setMyDescription
import eu.vendeli.tgbot.api.botactions.setMyName
import eu.vendeli.tgbot.api.botactions.setMyShortDescription
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.onFailure
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class SetMyActionsTest : BotTestContext() {
    @Test
    suspend fun `set my default administrator rights method testing`() {
        setMyDefaultAdministratorRights(
            ChatAdministratorRights(
                isAnonymous = true,
                canManageChat = false,
                canDeleteMessages = false,
                canRestrictMembers = false,
                canPromoteMembers = false,
                canChangeInfo = false,
                canInviteUsers = false,
                canPostMessages = false,
                canEditMessages = false,
                canPinMessages = false,
                canManageTopics = false,
            ),
        ).send(bot)

        val result = getMyDefaultAdministratorRights().sendAsync(bot).shouldSuccess()

        with(result) {
            isAnonymous.shouldBeTrue()
        }

        setMyDefaultAdministratorRights(
            ChatAdministratorRights(
                isAnonymous = false,
                canManageChat = false,
                canDeleteMessages = false,
                canRestrictMembers = false,
                canPromoteMembers = false,
                canChangeInfo = false,
                canInviteUsers = false,
                canPostMessages = false,
                canEditMessages = false,
                canPinMessages = false,
                canManageTopics = false,
            ),
        ).send(bot)
    }

    @Test
    suspend fun `set description method testing`() {
        setMyDescription("test").send(bot)
        val result = getMyDescription().sendAsync(bot).shouldSuccess()

        result.shouldNotBeNull()
        result.description shouldBe "test"

        setMyDescription().send(bot)
    }

    @Test
    suspend fun `set short description method testing`() {
        setMyShortDescription("test").send(bot)
        val result = getMyShortDescription().sendAsync(bot).shouldSuccess()

        result.shouldNotBeNull()
        result.shortDescription shouldBe "test"
        setMyShortDescription().send(bot)
    }

    @Test
    suspend fun `set my commands method testing`() {
        setMyCommands {
            botCommand("test", "testD")
        }.send(bot)
        val result = getMyCommands().sendAsync(bot).shouldSuccess()

        result.shouldNotBeNull()
        result.shouldNotBeEmpty()
        result shouldContain BotCommand("test", "testD")
        deleteMyCommands().send(bot)
    }

    @Test
    suspend fun `delete my commands method testing`() {
//        setMyCommands("en", BotCommandScope.AllPrivateChats) {
//            "test" description "testD"
//        }.send(bot)
//        getMyCommands("en", BotCommandScope.AllPrivateChats).sendAsync(bot)
//            .await().getOrNull().shouldNotBeNull().shouldNotBeEmpty()

        val result = deleteMyCommands().sendAsync(bot).shouldSuccess()

        result.shouldNotBeNull()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `set my name method testing`() {
        val request = setMyName("testbot2").sendAsync(bot).await()

        request.onFailure {
            if (it.errorCode == 429) return // delay due to limit
        }

        val result = request.shouldSuccess()

        result.shouldNotBeNull()
        result.shouldBeTrue()

        getMyName()
            .sendAsync(bot)
            .await()
            .getOrNull()
            .shouldNotBeNull()
            .name shouldBe "testbot2"

        setMyName("testbot").send(bot)
    }
}
