package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.builders.EntitiesBuilder
import eu.vendeli.tgbot.utils.encodeWith

/**
 * Entities feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Action Action class itself.
 */
interface EntitiesFeature<Action : TgAction<*>> : Feature {
    /**
     * Entities adding DSL
     */
    fun entities(block: EntitiesBuilder.() -> Unit): Action = entities(EntitiesBuilder.build(block))

    /**
     * Add Entities directly
     */
    @Suppress("UNCHECKED_CAST")
    fun entities(entities: List<MessageEntity>): Action = (this as Action).apply {
        parameters[entitiesFieldName] = entities.encodeWith(MessageEntity.serializer())
    }
}
