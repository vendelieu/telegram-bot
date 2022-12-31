@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.SetWebhookOptions

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
