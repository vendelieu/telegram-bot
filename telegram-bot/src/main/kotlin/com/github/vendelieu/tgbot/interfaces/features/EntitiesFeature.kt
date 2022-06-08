package com.github.vendelieu.tgbot.interfaces.features

import com.github.vendelieu.tgbot.types.MessageEntity
import com.github.vendelieu.tgbot.utils.EntitiesBuilder

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
