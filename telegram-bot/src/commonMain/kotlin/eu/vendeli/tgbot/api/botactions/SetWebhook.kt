@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.SetWebhookOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SetWebhookAction(
    url: String,
) : SimpleAction<Boolean>(),
    OptionsFeature<SetWebhookAction, SetWebhookOptions> {
    override val method = "setWebhook"
    override val returnType = getReturnType()
    override val options = SetWebhookOptions()
    override val beforeReq: () -> Unit = {
        options.certificate?.also { handleImplicitFile(it.toImplicitFile(), "certificate") }
    }

    init {
        parameters["url"] = url.toJsonElement()
    }
}

/**
 * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized Update. In case of an unsuccessful request, we will give up after a reasonable amount of attempts. Returns True on success.
 * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter secret_token. If specified, the request will contain a header "X-Telegram-Bot-Api-Secret-Token" with the secret token as content.
 *
 * [Api reference](https://core.telegram.org/bots/api#setwebhook)
 * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration
 * @param certificate Upload your public key certificate so that the root certificate in use can be checked. See our self-signed guide for details.
 * @param ipAddress The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
 * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to 40. Use lower values to limit the load on your bot's server, and higher values to increase your bot's throughput.
 * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify ["message", "edited_channel_post", "callback_query"] to only receive updates of these types. See Update for a complete list of available update types. Specify an empty list to receive all update types except chat_member, message_reaction, and message_reaction_count (default). If not specified, the previous setting will be used. Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted updates may be received for a short period of time.
 * @param dropPendingUpdates Pass True to drop all pending updates
 * @param secretToken A secret token to be sent in a header "X-Telegram-Bot-Api-Secret-Token" in every webhook request, 1-256 characters. Only characters A-Z, a-z, 0-9, _ and - are allowed. The header is useful to ensure that the request comes from a webhook set by you.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setWebhook(url: String) = SetWebhookAction(url)
