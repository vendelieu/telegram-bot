package eu.vendeli.api.forum

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.forum.closeGeneralForumTopic
import eu.vendeli.tgbot.api.forum.editGeneralForumTopic
import eu.vendeli.tgbot.api.forum.getForumTopicIconStickers
import eu.vendeli.tgbot.api.forum.hideGeneralForumTopic
import eu.vendeli.tgbot.api.forum.reopenGeneralForumTopic
import eu.vendeli.tgbot.api.forum.unhideGeneralForumTopic
import eu.vendeli.tgbot.api.forum.unpinAllGeneralForumTopicMessages
import eu.vendeli.tgbot.types.component.onFailure
import eu.vendeli.tgbot.types.media.StickerType
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe

@EnabledIf(ChatTestingOnlyCondition::class)
class GeneralTopicActionsTest : BotTestContext() {
    @Test
    suspend fun `edit general topic method test`() {
        val result = editGeneralForumTopic("General1").sendReq(CHAT_ID).shouldSuccess()

        result.shouldBeTrue()
        editGeneralForumTopic("General").send(CHAT_ID, bot)
    }

    @Test
    suspend fun `hide general topic method test`() {
        val result = hideGeneralForumTopic().sendReq(CHAT_ID).shouldSuccess()

        result.shouldBeTrue()
        unhideGeneralForumTopic().sendReq(CHAT_ID)
    }

    @Test
    suspend fun `unhide general topic method test`() {
        hideGeneralForumTopic().sendReq(CHAT_ID)
        val request = unhideGeneralForumTopic().sendReq(CHAT_ID)
        request.onFailure {
            if (it.errorCode == 429) return // skip if limit exceeded
        }

        request.shouldSuccess().shouldBeTrue()
    }

    @Test
    suspend fun `close general topic method test`() {
        reopenGeneralForumTopic().sendReq(CHAT_ID)
        val result = closeGeneralForumTopic().sendReq(CHAT_ID).shouldSuccess()

        result.shouldBeTrue()
        reopenGeneralForumTopic().send(CHAT_ID, bot)
    }

    @Test
    suspend fun `open general topic method test`() {
        closeGeneralForumTopic().send(CHAT_ID, bot)
        val result = reopenGeneralForumTopic().sendReq(CHAT_ID).shouldSuccess()

        result.shouldBeTrue()
    }

    @Test
    suspend fun `get forum topic icons method test`() {
        val result = getForumTopicIconStickers().sendReq(CHAT_ID).shouldSuccess()

        result.shouldNotBeEmpty()

        with(result.first()) {
            type shouldBe StickerType.CustomEmoji
            customEmojiId shouldBe "5434144690511290129"
        }
    }

    @Test
    suspend fun `unpin all forum general topic method test`() {
        val result = unpinAllGeneralForumTopicMessages().sendReq(CHAT_ID).shouldSuccess()

        result.shouldBeTrue()
    }
}
