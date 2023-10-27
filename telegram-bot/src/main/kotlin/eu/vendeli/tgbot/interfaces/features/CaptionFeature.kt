package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.IActionState
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.builders.EntitiesBuilder
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder

/**
 * Caption feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action class itself.
 */
interface CaptionFeature<Return : EntitiesContextBuilder> : IActionState, Feature {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * DSL for adding captions
     *
     * @param block
     * @return [Return]
     */
    fun caption(block: EntitiesContextBuilder.() -> String): Return {
        parameters["caption"] = block(thisAsReturn)
        return thisAsReturn
    }

    /**
     * Caption entities
     */
    fun captionEntities(entities: List<MessageEntity>): Return {
        parameters["caption_entities"] = entities
        return thisAsReturn
    }

    /**
     * Caption entities DSL with [EntitiesBuilder]
     */
    fun captionEntities(block: EntitiesBuilder.() -> Unit): Return =
        captionEntities(EntitiesBuilder().apply(block).listOfEntities)
}
