package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.EntityType
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.poll.InputPollOption

class PollOptionsBuilder {
    private val options: MutableList<InputPollOption> = mutableListOf()
    private var entitiesList: MutableList<MessageEntity> = mutableListOf()

    fun option(parseMode: ParseMode? = null, block: () -> String) {
        options.add(InputPollOption(block(), parseMode, entitiesList.takeIf { it.isNotEmpty() }))
        entitiesList = mutableListOf()
    }

    fun String.customEmoji(id: String, emojiLength: Int = 2): String {
        entitiesList.add(MessageEntity(EntityType.CustomEmoji, length, emojiLength, customEmojiId = id))
        return this
    }

    internal companion object {
        fun build(block: PollOptionsBuilder.() -> Unit) = PollOptionsBuilder().apply(block).options
    }
}
