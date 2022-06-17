package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.EntitiesBuilder

interface EntitiesFeature<Return : EntityAble> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    fun entities(block: EntitiesBuilder.() -> Unit): Return {
        parameters["entities"] = EntitiesBuilder().apply(block).listOfEntities
        return thisAsReturn
    }

    fun entities(entities: List<MessageEntity>): Return {
        parameters["entities"] = entities
        return thisAsReturn
    }
}
