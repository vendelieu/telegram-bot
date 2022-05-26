package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.*
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Update
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.GetUpdatesOptions

class GetUpdatesAction :
    SimpleAction<List<Update>>,
    OptionAble,
    OptionsFeature<GetUpdatesAction, GetUpdatesOptions>,
    MultiResponseOf<Update> {
    override val method: TgMethod = TgMethod("getUpdates")
    override var options = GetUpdatesOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : MultipleResponse> TgAction.bunchResponseInnerType(): Class<T> = getInnerType() as Class<T>
}

fun getUpdates() = GetUpdatesAction()
