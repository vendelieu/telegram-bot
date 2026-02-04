package eu.vendeli.api.story

import BotTestContext
import eu.vendeli.tgbot.api.story.deleteStory
import eu.vendeli.tgbot.api.story.editStory
import eu.vendeli.tgbot.api.story.postStory
import eu.vendeli.tgbot.api.story.repostStory
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.story.InputStoryContent
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.runBlocking
import utils.RandomPicResource
import kotlin.time.Duration.Companion.seconds

class StoryApiTest : BotTestContext() {
    private val image: ImplicitFile by lazy {
        runBlocking {
            bot.httpClient
                .get(RandomPicResource.current.getPicUrl(400, 400))
                .readRawBytes()
                .toImplicitFile("test.png", "image/png")
        }
    }

    @Test
    @Ignore // todo fix and return; possible related to serde
    suspend fun `postStory test`()  {
        postStory("test", InputStoryContent.Photo(image), 10.seconds)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    @Ignore // todo fix and return
    suspend fun `editStory test`()  {
        editStory("test", "story_id", InputStoryContent.Photo(image))
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `deleteStory test`()  {
        deleteStory("test", "story_id")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    suspend fun `repostStory test`()  {
        repostStory("test", 123456L, 1, 86400.seconds)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }
}
