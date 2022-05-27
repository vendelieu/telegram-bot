package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.SetWebhookOptions

class SetWebhookAction(url: String) :
    SimpleAction<Boolean>,
    OptionAble,
    OptionsFeature<SetWebhookAction, SetWebhookOptions> {
    override val method: TgMethod = TgMethod("setWebhook")
    override var options = SetWebhookOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["url"] = url
    }
}

fun setWebhook(url: String) = SetWebhookAction(url)
