package com.github.vendelieu.tgbot.interfaces.features

import com.github.vendelieu.tgbot.interfaces.Keyboard

interface MarkupFeature<Return : MarkupAble> : Feature {
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    fun markup(keyboard: Keyboard): Return {
        parameters["reply_markup"] = keyboard
        return thisAsReturn
    }

    fun markup(block: () -> Keyboard): Return = markup(block())
}
