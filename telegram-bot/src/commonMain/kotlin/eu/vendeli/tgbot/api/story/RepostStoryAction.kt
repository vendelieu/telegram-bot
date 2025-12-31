@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.story

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.media.Story
import eu.vendeli.tgbot.types.options.RepostStoryOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlin.time.Duration

@TgAPI
class RepostStoryAction(
    businessConnectionId: String,
    fromChatId: Long,
    fromStoryId: Int,
    activePeriod: Int,
) : SimpleAction<Story>(),
    OptionsFeature<RepostStoryAction, RepostStoryOptions> {
    @TgAPI.Name("repostStory")
    override val method = "repostStory"
    override val returnType = getReturnType()
    override val options = RepostStoryOptions()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["from_chat_id"] = fromChatId.toJsonElement()
        parameters["from_story_id"] = fromStoryId.toJsonElement()
        parameters["active_period"] = activePeriod.toJsonElement()
    }
}

/**
 * Reposts a story on behalf of a business account from another business account. Both business accounts must be managed by the same bot, and the story on the source account must have been posted (or reposted) by the bot. Requires the can_manage_stories business bot right for both business accounts. Returns Story on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#repoststory)
 * @param businessConnectionId Unique identifier of the business connection
 * @param fromChatId Unique identifier of the chat which posted the story that should be reposted
 * @param fromStoryId Unique identifier of the story that should be reposted
 * @param activePeriod Period after which the story is moved to the archive, in seconds; must be one of 6 * 3600, 12 * 3600, 86400, or 2 * 86400
 * @param postToChatPage Pass True to keep the story accessible after it expires
 * @param protectContent Pass True if the content of the story must be protected from forwarding and screenshotting
 * @returns [Story]
 */
@TgAPI
inline fun repostStory(
    businessConnectionId: String,
    fromChatId: Long,
    fromStoryId: Int,
    activePeriod: Int,
) = RepostStoryAction(businessConnectionId, fromChatId, fromStoryId, activePeriod)

@TgAPI
inline fun repostStory(
    businessConnectionId: String,
    fromChatId: Long,
    fromStoryId: Int,
    activePeriod: Duration,
) = RepostStoryAction(businessConnectionId, fromChatId, fromStoryId, activePeriod.inWholeSeconds.toInt())

