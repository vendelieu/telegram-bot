package com.github.vendelieu.tgbot.interfaces

import com.github.vendelieu.tgbot.types.internal.TgMethod

interface TgAction {
    val method: TgMethod

    fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T>? = null
}
