@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.bot.WebhookInfo
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetWebhookInfoAction : SimpleAction<WebhookInfo>() {
    @TgAPI.Name("getWebhookInfo")
    override val method = "getWebhookInfo"
    override val returnType = getReturnType()
}

/**
 * Use this method to get current webhook status. Requires no parameters. On success, returns a WebhookInfo object. If the bot is using getUpdates, will return an object with the url field empty.
 *
 * [Api reference](https://core.telegram.org/bots/api#getwebhookinfo)
 *
 * @returns [WebhookInfo]
 */
@TgAPI
inline fun getWebhookInfo() = GetWebhookInfoAction()
