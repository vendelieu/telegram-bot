package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.WebhookInfo
import eu.vendeli.tgbot.types.internal.TgMethod

class GetWebhookInfoAction : SimpleAction<WebhookInfo> {
    override val method: TgMethod = TgMethod("getWebhookInfo")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun getWebhookInfo() = GetWebhookInfoAction()
