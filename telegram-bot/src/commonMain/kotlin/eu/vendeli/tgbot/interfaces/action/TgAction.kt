package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.types.options.Options
import eu.vendeli.tgbot.utils.internal.makeRequestReturning
import eu.vendeli.tgbot.utils.internal.makeSilentRequest
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.tgbot.utils.internal.toJsonElement
import io.ktor.http.content.PartData
import kotlinx.coroutines.Deferred
import kotlinx.serialization.KSerializer
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
        require(multipartData.isEmpty()) { "Multipart files is not supported for webhook response." }
        parameters["method"] = method.toJsonElement()
        return serde.encodeToString(parameters)
    }

    /**
     * A method that is implemented in Action.
     */
    internal abstract val method: String

    /**
     * Type of action result.
     */
    protected abstract val returnType: KSerializer<ReturnType>

    /**
     * The parameter that stores the options.
     */
    internal open val options: Options by Delegates.notNull()

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

    @KtGramInternal
    override suspend fun Request<ReturnType>.doRequest(bot: TelegramBot) {
        beforeReq()
        bot.makeSilentRequest(method, parameters, multipartData)
    }

    @KtGramInternal
    override suspend fun Request<ReturnType>.doRequestReturning(bot: TelegramBot): Deferred<Response<out ReturnType>> {
        beforeReq()
        return bot.makeRequestReturning(method, parameters, returnType, multipartData)
    }

    @KtGramInternal
    override val Request<ReturnType>.parameters: MutableMap<String, JsonElement>
        get() = this@TgAction.parameters

    @KtGramInternal
    val Request<ReturnType>.methodName: String get() = method
}
