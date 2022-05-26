package com.github.vendelieu.tgbot.interfaces.features

import com.github.vendelieu.tgbot.types.MessageEntity

interface CaptionFeature<Return : CaptionAble> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    fun caption(block: () -> String): Return {
        parameters["caption"] = block()
        return thisAsReturn
    }

    fun captionEntities(block: () -> Array<MessageEntity>): Return {
        parameters["caption_entities"] = block()
        return thisAsReturn
    }
}
