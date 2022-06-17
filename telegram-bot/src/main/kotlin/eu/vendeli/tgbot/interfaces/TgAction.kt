package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.types.internal.TgMethod

interface TgAction {
    val method: TgMethod

    fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T>? = null
}
