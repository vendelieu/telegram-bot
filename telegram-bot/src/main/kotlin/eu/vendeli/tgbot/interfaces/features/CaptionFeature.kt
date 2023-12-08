package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.builders.EntitiesBuilder
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder

/**
 * Caption feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action class itself.
 */
interface CaptionFeature<Return> : Feature where Return : EntitiesContextBuilder, Return : TgAction<*> {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * DSL for adding captions
     *
     * @param block
     * @return [Return]
     */
    fun caption(block: EntitiesContextBuilder.() -> String): Return = thisAsReturn.apply {
        parameters["caption"] = block(thisAsReturn)
    }

    /**
     * Caption entities
     */
    fun captionEntities(entities: List<MessageEntity>): Return = thisAsReturn.apply {
        parameters["caption_entities"] = entities
    }

    /**
     * Caption entities DSL with [EntitiesBuilder]
     */
    fun captionEntities(block: EntitiesBuilder.() -> Unit): Return = captionEntities(EntitiesBuilder.build(block))
}
