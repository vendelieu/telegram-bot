@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.story

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.media.Story
import eu.vendeli.tgbot.types.options.PostStoryOptions
import eu.vendeli.tgbot.types.story.InputStoryContent
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlin.time.Duration

@TgAPI
class PostStoryAction(
    businessConnectionId: String,
    content: InputStoryContent,
    activePeriod: Duration,
) : SimpleAction<Story>(),
    CaptionFeature<PostStoryAction>,
    OptionsFeature<PostStoryAction, PostStoryOptions> {
    @TgAPI.Name("postStory")
    override val method = "postStory"
    override val returnType = getReturnType()
    override val options = PostStoryOptions()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["content"] = content.encodeWith(InputStoryContent.serializer())
        parameters["active_period"] = activePeriod.encodeWith(DurationSerializer)
    }
}

/**
 * Posts a story on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#poststory)
 * @param businessConnectionId Unique identifier of the business connection
 * @param content Content of the story
 * @param activePeriod Period after which the story is moved to the archive, in seconds; must be one of 6 * 3600, 12 * 3600, 86400, or 2 * 86400
 * @param caption Caption of the story, 0-2048 characters after entities parsing
 * @param parseMode Mode for parsing entities in the story caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param areas A JSON-serialized list of clickable areas to be shown on the story
 * @param postToChatPage Pass True to keep the story accessible after it expires
 * @param protectContent Pass True if the content of the story must be protected from forwarding and screenshotting
 * @returns [Story]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun postStory(
    businessConnectionId: String,
    content: InputStoryContent,
    activePeriod: Duration,
) = PostStoryAction(businessConnectionId, content, activePeriod)
