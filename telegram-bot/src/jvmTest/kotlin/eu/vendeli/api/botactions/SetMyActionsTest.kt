package eu.vendeli.api.botactions

import BotTestContext
import eu.vendeli.tgbot.api.botactions.deleteMyCommands
import eu.vendeli.tgbot.api.botactions.getMyCommands
import eu.vendeli.tgbot.api.botactions.getMyDescription
import eu.vendeli.tgbot.api.botactions.getMyName
import eu.vendeli.tgbot.api.botactions.getMyShortDescription
import eu.vendeli.tgbot.api.botactions.setMyCommands
import eu.vendeli.tgbot.api.botactions.setMyDefaultAdministratorRights
import eu.vendeli.tgbot.api.botactions.setMyDescription
import eu.vendeli.tgbot.api.botactions.removeMyProfilePhoto
import eu.vendeli.tgbot.api.botactions.setMyName
import eu.vendeli.tgbot.api.botactions.setMyProfilePhoto
import eu.vendeli.tgbot.api.botactions.setMyShortDescription
import eu.vendeli.tgbot.types.bot.BotCommand
import eu.vendeli.tgbot.types.bot.BotCommandScope
import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import eu.vendeli.tgbot.types.media.InputProfilePhoto
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.tgbot.types.component.onFailure
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import utils.RandomPicResource

class SetMyActionsTest : BotTestContext() {
    @Test
    suspend fun `set my default administrator rights method testing`() {
        setMyDefaultAdministratorRights(
            ChatAdministratorRights(
                isAnonymous = true,
                canManageChat = true,
                canDeleteMessages = false,
                canRestrictMembers = false,
                canPromoteMembers = false,
                canChangeInfo = false,
                canInviteUsers = false,
                canPostMessages = false,
                canEditMessages = false,
                canPinMessages = false,
                canManageTopics = false,
                canManageVideoChats = false,
                canPostStories = false,
                canEditStories = false,
                canDeleteStories = false,
            ),
        ).sendReq(bot).shouldSuccess().shouldBeTrue()
    }

    @Test
    suspend fun `set description method testing`() {
        setMyDescription("test").send(bot)
        val result = getMyDescription().sendReq().shouldSuccess()

        result.shouldNotBeNull()
        result.description shouldBe "test"

        setMyDescription().send(bot)
    }

    @Test
    suspend fun `set short description method testing`() {
        setMyShortDescription("test").send(bot)
        val result = getMyShortDescription().sendReq().shouldSuccess()

        result.shouldNotBeNull()
        result.shortDescription shouldBe "test"
        setMyShortDescription().send(bot)
    }

    @Test
    suspend fun `set my commands method testing`() {
        setMyCommands {
            botCommand("test", "testD")
            "test2" description "testC"
        }.send(bot)
        val result = getMyCommands().sendReq().shouldSuccess()

        result.shouldNotBeNull()
        result.shouldNotBeEmpty()
        result shouldContain BotCommand("test", "testD")
        deleteMyCommands().send(bot)
    }

    @Test
    suspend fun `delete my commands method testing`() {
        setMyCommands("en", BotCommandScope.AllPrivateChats) {
            "test" description "testD"
        }.send(bot)

        getMyCommands("en", BotCommandScope.AllPrivateChats)
            .sendReq()
            .shouldSuccess()
            .shouldNotBeNull()
            .shouldNotBeEmpty()

        val result = deleteMyCommands().sendReq().shouldSuccess()

        result.shouldNotBeNull()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `set my name method testing`() {
        val request = setMyName("testbot2").sendReq()

        request.onFailure {
            if (it.errorCode == 429) return // delay due to limit
        }

        val result = request.shouldSuccess()

        result.shouldNotBeNull()
        result.shouldBeTrue()

        getMyName()
            .sendReq()
            .shouldSuccess()
            .name shouldBe "testbot2"

        setMyName("testbot").send(bot)
    }

    @Test
    @Ignore
    suspend fun `set my profile photo method testing`() {
        val image = bot.httpClient
            .get(RandomPicResource.current.getPicUrl(400, 400))
            .readRawBytes()
            .toImplicitFile("test.png", "image/png")

        val result = setMyProfilePhoto(InputProfilePhoto.Static(image)).sendReq()
        result.onFailure {
            if (it.errorCode == 429) return
        }
        result.shouldSuccess().shouldBeTrue()

        removeMyProfilePhoto().sendReq().shouldSuccess().shouldBeTrue()
    }

    @Test
    @Ignore
    suspend fun `remove my profile photo method testing`() {
        val image = bot.httpClient
            .get(RandomPicResource.current.getPicUrl(400, 400))
            .readRawBytes()
            .toImplicitFile("test.png", "image/png")
        setMyProfilePhoto(InputProfilePhoto.Static(image)).sendReq().onFailure { return }

        removeMyProfilePhoto().sendReq().shouldSuccess().shouldBeTrue()
    }
}
