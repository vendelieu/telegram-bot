@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.WebhookInfo
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetWebhookInfoAction : SimpleAction<WebhookInfo>, ActionState() {
    override val method: TgMethod = TgMethod("getWebhookInfo")
    override val returnType = getReturnType()
}

fun getWebhookInfo() = GetWebhookInfoAction()
