package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.WebhookInfo
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetWebhookInfoAction : SimpleAction<WebhookInfo> {
    override val method: TgMethod = TgMethod("getWebhookInfo")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun getWebhookInfo() = GetWebhookInfoAction()
