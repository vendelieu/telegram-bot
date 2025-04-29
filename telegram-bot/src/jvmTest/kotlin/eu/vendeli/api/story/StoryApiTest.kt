package eu.vendeli.api.story

import BotTestContext
import eu.vendeli.tgbot.api.story.deleteStory
import eu.vendeli.tgbot.api.story.editStory
import eu.vendeli.tgbot.api.story.postStory
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.story.InputStoryContent
import eu.vendeli.tgbot.utils.common.toImplicitFile
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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
    fun `postStory test`() = runTest {
        postStory("test", InputStoryContent.Photo(image), 10.seconds)
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `editStory test`() = runTest {
        editStory("test", "story_id", InputStoryContent.Photo(image))
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }

    @Test
    fun `deleteStory test`() = runTest {
        deleteStory("test", "story_id")
            .sendReq()
            .shouldFailure()
            .description
            .shouldBe("Bad Request: business connection not found")
    }
}
