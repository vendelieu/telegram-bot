package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.Options
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import io.ktor.http.content.PartData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlin.properties.Delegates

/**
 * Tg action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 */
abstract class TgAction<ReturnType> : Request<ReturnType> {
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

    @InternalApi
    override suspend fun Request<ReturnType>.doRequest(bot: TelegramBot) {
        bot.makeSilentRequest(method, parameters, multipartData)
    }

    @InternalApi
    override suspend fun Request<ReturnType>.doRequestReturning(bot: TelegramBot) =
        bot.makeRequestAsync(method, parameters, returnType, multipartData)

    @InternalApi
    override val Request<ReturnType>.parameters: MutableMap<String, JsonElement>
        get() = this@TgAction.parameters
}
