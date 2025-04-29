@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.story

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class DeleteStoryAction(
    businessConnectionId: String,
    storyId: String,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("deleteStory")
    override val method = "deleteStory"
    override val returnType = getReturnType()
    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["story_id"] = storyId.toJsonElement()
    }
}

/**
 * Deletes a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletestory)
 * @param businessConnectionId Unique identifier of the business connection
 * @param storyId Unique identifier of the story to delete
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun deleteStory(
    businessConnectionId: String,
    storyId: String,
) = DeleteStoryAction(businessConnectionId, storyId)
