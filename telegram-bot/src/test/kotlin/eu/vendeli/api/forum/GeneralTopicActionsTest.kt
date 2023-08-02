package eu.vendeli.api.forum

import BotTestContext
import eu.vendeli.tgbot.api.forum.closeGeneralForumTopic
import eu.vendeli.tgbot.api.forum.getForumTopicIconStickers
import eu.vendeli.tgbot.api.forum.hideGeneralForumTopic
import eu.vendeli.tgbot.api.forum.reopenGeneralForumTopic
import eu.vendeli.tgbot.api.forum.unhideGeneralForumTopic
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.media.StickerType
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class GeneralTopicActionsTest: BotTestContext() {
    @Test
    suspend fun `hide general topic method test`() {
        val request = hideGeneralForumTopic().sendAsync(CHAT_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldBeTrue()
        unhideGeneralForumTopic().send(CHAT_ID, bot)
    }

    @Test
    suspend fun `unhide general topic method test`() {
        hideGeneralForumTopic().send(CHAT_ID, bot)
        val request = unhideGeneralForumTopic().sendAsync(CHAT_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldBeTrue()
    }

    @Test
    suspend fun `close general topic method test`() {
        val request = closeGeneralForumTopic().sendAsync(CHAT_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldBeTrue()
    }
    @Test
    suspend fun `open general topic method test`() {
        val request = reopenGeneralForumTopic().sendAsync(CHAT_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldBeTrue()
    }

    @Test
    suspend fun `get forum topic icons method test`() {
        val request = getForumTopicIconStickers().sendAsync(CHAT_ID, bot).await()

        val result = with(request) {
            ok.shouldBeTrue()
            isSuccess().shouldBeTrue()
            getOrNull().shouldNotBeNull()
        }
        result.shouldNotBeEmpty()

        with(result.first()) {
            type shouldBe StickerType.CustomEmoji
            customEmojiId shouldBe "5434144690511290129"
        }
    }
}
