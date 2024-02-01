@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.WebhookInfo
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetWebhookInfoAction : SimpleAction<WebhookInfo>() {
    override val method = TgMethod("getWebhookInfo")
    override val returnType = getReturnType()
}

inline fun getWebhookInfo() = GetWebhookInfoAction()
