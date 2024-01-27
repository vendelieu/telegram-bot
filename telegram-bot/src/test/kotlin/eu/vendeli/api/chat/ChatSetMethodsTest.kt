package eu.vendeli.api.chat

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.botactions.getMe
import eu.vendeli.tgbot.api.chat.deleteChatPhoto
import eu.vendeli.tgbot.api.chat.deleteChatStickerSet
import eu.vendeli.tgbot.api.chat.promoteChatMember
import eu.vendeli.tgbot.api.chat.setChatAdministratorCustomTitle
import eu.vendeli.tgbot.api.chat.setChatDescription
import eu.vendeli.tgbot.api.chat.setChatMenuButton
import eu.vendeli.tgbot.api.chat.setChatPermissions
import eu.vendeli.tgbot.api.chat.setChatPhoto
import eu.vendeli.tgbot.api.chat.setChatStickerSet
import eu.vendeli.tgbot.api.chat.setChatTitle
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.onFailure
import eu.vendeli.tgbot.types.keyboard.MenuButton
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes

@EnabledIf(ChatTestingOnlyCondition::class)
class ChatSetMethodsTest : BotTestContext() {
    @Test
    suspend fun `set chat custom title method test`() {
        promoteChatMember(TG_ID.asUser()).options {
            canDeleteMessages = true
        }.sendReturning(CHAT_ID, bot).shouldSuccess().shouldBeTrue()

        val result = setChatAdministratorCustomTitle(TG_ID, "test").sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
        setChatAdministratorCustomTitle(TG_ID, "").sendReturning(CHAT_ID, bot).shouldSuccess()
        promoteChatMember(TG_ID).sendReturning(CHAT_ID, bot).shouldSuccess().shouldBeTrue()
    }

    @Test
    suspend fun `set chat description method test`() {
        val result = setChatDescription("Test $RAND_INT").sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
        setChatDescription().sendReturning(CHAT_ID, bot).shouldSuccess()
    }

    @Test
    suspend fun `set chat menu button method test`() {
        val result = setChatMenuButton(MenuButton.Commands()).sendReturning(BOT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
        setChatMenuButton(MenuButton.Default()).sendReturning(BOT_ID, bot).shouldSuccess()
    }

    @Test
    suspend fun `set chat permissions method test`() {
        val result = setChatPermissions(true) {
            canSendMessages = true
            canSendAudios = true
            canSendDocuments = true
            canSendPhotos = true
            canSendVideos = true
            canSendVideoNotes = true
            canSendVoiceNotes = true
            canSendPolls = true
            canSendOtherMessages = true
            canAddWebPagePreviews = true
            canChangeInfo = true
            canInviteUsers = true
            canPinMessages = true
            canManageTopics = true
            canChangeInfo = true
        }.sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `set chat photo method test`() {
        val file = bot.httpClient.get("https://random.imagecdn.app/200/200").readBytes()
        val result = setChatPhoto(file).sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()

        deleteChatPhoto().sendReturning(CHAT_ID, bot).shouldSuccess()
    }

    @Test
    suspend fun `set chat sticker set method test`() {
        val botName = getMe().sendAsync(bot).await().getOrNull().shouldNotBeNull().username.shouldNotBeNull()
        val setName = "Test_2_by_$botName"

        val result = setChatStickerSet(setName).sendReturning(CHAT_ID, bot).onFailure {
            if (it.description?.contains("can't set supergroup sticker set") == true) return
        }.shouldNotBeNull()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `delete chat sticker set method test`() {
        val result = deleteChatStickerSet().sendReturning(CHAT_ID, bot).onFailure {
            if (it.description?.contains("can't set supergroup sticker set") == true) return
        }.shouldNotBeNull()
        result.shouldBeTrue()
    }

    @Test
    suspend fun `set chat title method test`() {
        val result = setChatTitle("test").sendReturning(CHAT_ID, bot).shouldSuccess()
        result.shouldBeTrue()
        setChatTitle("testchat").sendReturning(CHAT_ID, bot).shouldSuccess()
    }
}
