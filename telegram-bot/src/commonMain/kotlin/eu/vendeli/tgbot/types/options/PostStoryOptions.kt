package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.story.StoryArea
import kotlinx.serialization.Serializable

@Serializable
data class PostStoryOptions(
    var postToChatPage: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var areas: List<StoryArea>? = null,
    override var protectContent: Boolean? = null,
) : Options,
    OptionsParseMode,
    ProtectContentProp,
    AreasProp
