@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.SetWebhookOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import kotlinx.coroutines.Deferred

class SetWebhookAction(url: String) :
    SimpleAction<Boolean>(),
    OptionsFeature<SetWebhookAction, SetWebhookOptions> {
    override val method = TgMethod("setWebhook")
    override val returnType = getReturnType()
    override val options = SetWebhookOptions()

    init {
        parameters["url"] = url
    }

    override suspend fun send(to: TelegramBot) {
        to.makeSilentRequest(method, parameters, parameters["certificate"] != null)
    }

    override suspend fun sendAsync(to: TelegramBot): Deferred<Response<out Boolean>> = to.makeRequestAsync(
        method,
        parameters,
        returnType,
        wrappedDataType,
        parameters["certificate"] != null,
    )
}

@Suppress("NOTHING_TO_INLINE")
inline fun setWebhook(url: String) = SetWebhookAction(url)
