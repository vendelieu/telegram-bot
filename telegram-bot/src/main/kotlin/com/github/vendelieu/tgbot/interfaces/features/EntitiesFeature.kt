package com.github.vendelieu.tgbot.interfaces.features

import com.github.vendelieu.tgbot.types.MessageEntity

interface EntitiesFeature<Return : EntityAble> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    fun entities(block: () -> Array<MessageEntity>): Return {
        parameters["entities"] = block()
        return thisAsReturn
    }
}
