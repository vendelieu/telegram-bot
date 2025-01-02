package eu.vendeli.api.forum

import BotTestContext
import ChatTestingOnlyCondition
import eu.vendeli.tgbot.api.forum.closeForumTopic
import eu.vendeli.tgbot.api.forum.createForumTopic
import eu.vendeli.tgbot.api.forum.deleteForumTopic
import eu.vendeli.tgbot.api.forum.editForumTopic
import eu.vendeli.tgbot.api.forum.reopenForumTopic
import eu.vendeli.tgbot.api.forum.unpinAllForumTopicMessages
import eu.vendeli.tgbot.types.forum.IconColor
import io.kotest.core.annotation.EnabledIf
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes

@EnabledIf(ChatTestingOnlyCondition::class)
class ForumCrudTest : BotTestContext() {
    @BeforeClass
    suspend fun chillOut() {
        delay(1.minutes)
    }

    @Test
    suspend fun `create forum topic method test`() {
        val result = createForumTopic(
            "testTopic",
            IconColor.GREEN,
        ).sendReq(CHAT_ID).shouldSuccess()

        with(result) {
            shouldNotBeNull()
            name shouldBe "testTopic"
            iconColor shouldBe IconColor.GREEN
        }
        deleteForumTopic(result.messageThreadId).send(CHAT_ID, bot)
    }

    @Test
    suspend fun `edit forum topic method test`() {
        val topic = createForumTopic("testTopic")
            .sendReq(CHAT_ID)
            .shouldSuccess()
        topic.name shouldBe "testTopic"

        val result = editForumTopic(
            topic.messageThreadId,
            "testTopic2",
        ).sendReturning(CHAT_ID, bot).shouldSuccess()

        result.shouldBeTrue()
        deleteForumTopic(topic.messageThreadId).send(CHAT_ID, bot)
    }

    @Test
    suspend fun `delete forum topic method test`() {
        val topic = createForumTopic("testTopic")
            .sendReq(CHAT_ID)
            .shouldSuccess()
        val result = deleteForumTopic(topic.messageThreadId).sendReturning(CHAT_ID, bot).shouldSuccess()

        result.shouldBeTrue()
    }

    @Test
    suspend fun `close forum topic method test`() {
        val topic = createForumTopic("testTopic")
            .sendReq(CHAT_ID)
            .shouldSuccess()
        val result = closeForumTopic(topic.messageThreadId).sendReturning(CHAT_ID, bot).shouldSuccess()

        result.shouldBeTrue()
        deleteForumTopic(topic.messageThreadId).send(CHAT_ID, bot)
    }

    @Test
    suspend fun `open forum topic method test`() {
        val topic = createForumTopic("testTopic")
            .sendReq(CHAT_ID)
            .shouldSuccess()
        closeForumTopic(topic.messageThreadId).send(CHAT_ID, bot)
        val result = reopenForumTopic(topic.messageThreadId).sendReturning(CHAT_ID, bot).shouldSuccess()

        result.shouldBeTrue()
        deleteForumTopic(topic.messageThreadId).send(CHAT_ID, bot)
    }

    @Test
    suspend fun `unpin all forum topic method test`() {
        val topic = createForumTopic("testTopic")
            .sendReq(CHAT_ID)
            .shouldSuccess()
        val result = unpinAllForumTopicMessages(topic.messageThreadId).sendReturning(CHAT_ID, bot).shouldSuccess()

        result.shouldBeTrue()
        deleteForumTopic(topic.messageThreadId).send(CHAT_ID, bot)
    }
}
