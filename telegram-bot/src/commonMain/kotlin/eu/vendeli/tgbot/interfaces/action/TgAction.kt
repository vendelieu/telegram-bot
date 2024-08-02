package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.Options
import eu.vendeli.tgbot.utils.makeRequestReturning
import eu.vendeli.tgbot.utils.makeSilentRequest
import eu.vendeli.tgbot.utils.serde
import eu.vendeli.tgbot.utils.toJsonElement
import io.ktor.http.content.PartData
import kotlinx.coroutines.Deferred
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlin.properties.Delegates

/**
 * Tg action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 */
abstract class TgAction<ReturnType> : Request<ReturnType> {
    /**
     * Payload to make response for the webhook request as described
     * [there](https://core.telegram.org/bots/faq#how-can-i-make-requests-in-response-to-updates).
     */
    fun toWebhookResponse(): String {
        require(multipartData.isEmpty()) { "Multipart files is not supported for webhook response flow." }
        parameters["method"] = method.name.toJsonElement()
        return serde.encodeToString(parameters)
    }

    /**
     * A method that is implemented in Action.
     */
    internal open val method by Delegates.notNull<TgMethod>()

    /**
     * The parameter that stores the options.
     */
    internal open val options by Delegates.notNull<Options>()

    /**
     * Type of action result.
     */
    protected open val returnType by Delegates.notNull<KSerializer<ReturnType>>()

    /**
     * Field where entities should be stored.
     */
    internal open val entitiesFieldName = "entities"

    /**
     * Action data storage parameter.
     */
    internal val parameters: MutableMap<String, JsonElement> = mutableMapOf()

    /**
     * Multipart payload of request.
     */
    internal val multipartData: MutableList<PartData.BinaryItem> = mutableListOf()

    protected open val beforeReq = {}

    @InternalApi
    override suspend fun Request<ReturnType>.doRequest(bot: TelegramBot) {
        beforeReq()
        bot.makeSilentRequest(method, parameters, multipartData)
    }

    @InternalApi
    override suspend fun Request<ReturnType>.doRequestReturning(bot: TelegramBot): Deferred<Response<out ReturnType>> {
        beforeReq()
        return bot.makeRequestReturning(method, parameters, returnType, multipartData)
    }

    @InternalApi
    override val Request<ReturnType>.parameters: MutableMap<String, JsonElement>
        get() = this@TgAction.parameters
}
