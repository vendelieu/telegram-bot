@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.SetWebhookOptions
import eu.vendeli.tgbot.types.internal.toImplicitFile
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred

class SetWebhookAction(url: String) :
    SimpleAction<Boolean>(),
    OptionsFeature<SetWebhookAction, SetWebhookOptions> {
    override val method = TgMethod("setWebhook")
    override val returnType = getReturnType()
    override val options = SetWebhookOptions()

    init {
        parameters["url"] = url.toJsonElement()
    }

    override suspend fun send(to: TelegramBot) {
        handleCert()
        to.makeSilentRequest(method, parameters, multipartData)
    }

    override suspend fun sendAsync(to: TelegramBot): Deferred<Response<out Boolean>> {
        handleCert()
        return to.makeRequestAsync(
            method,
            parameters,
            returnType,
            multipartData,
        )
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun handleCert() {
        options.certificate?.also { handleImplicitFile(it.toImplicitFile(), "certificate") }
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setWebhook(url: String) = SetWebhookAction(url)
