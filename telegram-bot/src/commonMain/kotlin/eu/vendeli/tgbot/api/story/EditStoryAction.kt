@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.story

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.media.Story
import eu.vendeli.tgbot.types.options.EditStoryOptions
import eu.vendeli.tgbot.types.story.InputStoryContent
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditStoryAction(
    businessConnectionId: String,
    storyId: String,
    content: InputStoryContent,
) : SimpleAction<Story>(),
    CaptionFeature<EditStoryAction>,
    OptionsFeature<EditStoryAction, EditStoryOptions> {
    @TgAPI.Name("editStory")
    override val method = "editStory"
    override val returnType = getReturnType()
    override val options = EditStoryOptions()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["story_id"] = storyId.toJsonElement()
        parameters["content"] = content.encodeWith(InputStoryContent.serializer())
    }
}

/**
 * Edits a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#editstory)
 * @param businessConnectionId Unique identifier of the business connection
 * @param storyId Unique identifier of the story to edit
 * @param content Content of the story
 * @param caption Caption of the story, 0-2048 characters after entities parsing
 * @param parseMode Mode for parsing entities in the story caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param areas A JSON-serialized list of clickable areas to be shown on the story
 * @returns [Story]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun editStory(
    businessConnectionId: String,
    storyId: String,
    content: InputStoryContent,
) = EditStoryAction(businessConnectionId, storyId, content)
