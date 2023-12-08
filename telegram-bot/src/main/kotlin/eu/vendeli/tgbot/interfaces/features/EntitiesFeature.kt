package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.builders.EntitiesBuilder

/**
 * Entities feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action class itself.
 */
interface EntitiesFeature<Return> : ActionState, Feature {
    /**
     * Entities adding DSL
     */
    fun entities(block: EntitiesBuilder.() -> Unit): Return = entities(EntitiesBuilder.build(block))

    /**
     * Add Entities directly
     */
    @Suppress("UNCHECKED_CAST")
    fun entities(entities: List<MessageEntity>): Return = (this as Return).apply {
        parameters["entities"] = entities
    }
}
