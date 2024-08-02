@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.WebhookInfo
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetWebhookInfoAction : SimpleAction<WebhookInfo>() {
    override val method = TgMethod("getWebhookInfo")
    override val returnType = getReturnType()
}

/**
 * Use this method to get current webhook status. Requires no parameters. On success, returns a WebhookInfo object. If the bot is using getUpdates, will return an object with the url field empty.
 *
 * [Api reference](https://core.telegram.org/bots/api#getwebhookinfo)
 *
 * @returns [WebhookInfo]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun getWebhookInfo() = GetWebhookInfoAction()
