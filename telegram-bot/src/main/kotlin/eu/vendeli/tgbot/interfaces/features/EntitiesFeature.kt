package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.ParametersBase
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.EntitiesBuilder

/**
 * Entities feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action class itself.
 */
interface EntitiesFeature<Return : EntityAble> : ParametersBase {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * Entities adding DSL
     */
    fun entities(block: EntitiesBuilder.() -> Unit): Return {
        parameters["entities"] = EntitiesBuilder().apply(block).listOfEntities
        return thisAsReturn
    }

    /**
     * Add Entities directly
     */
    fun entities(entities: List<MessageEntity>): Return {
        parameters["entities"] = entities
        return thisAsReturn
    }
}
