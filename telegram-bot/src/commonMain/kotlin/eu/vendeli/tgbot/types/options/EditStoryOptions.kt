package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.story.StoryArea
import kotlinx.serialization.Serializable

@Serializable
data class EditStoryOptions(
    override var areas: List<StoryArea>? = null,
    override var parseMode: ParseMode? = null,
) : Options,
    OptionsParseMode,
    AreasProp
