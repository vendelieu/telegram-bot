package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.EntitiesBuilder

/**
 * Caption feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action class itself.
 */
interface CaptionFeature<Return : CaptionAble> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * DSL for adding captions
     *
     * @param block
     * @return [Return]
     */
    fun caption(block: () -> String): Return {
        parameters["caption"] = block()
        return thisAsReturn
    }

    /**
     * Caption entities
     */
    fun captionEntities(block: () -> Array<MessageEntity>): Return {
        parameters["caption_entities"] = block()
        return thisAsReturn
    }

    /**
     * Caption entities DSL with [EntitiesBuilder]
     */
    fun captionEntities(block: EntitiesBuilder.() -> Unit): Return {
        parameters["caption_entities"] = EntitiesBuilder().apply(block).listOfEntities

        return thisAsReturn
    }
}
