package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.Recipient
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.toContentType
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import io.ktor.http.ContentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import kotlin.collections.set

/**
 * Media action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
interface MediaAction<ReturnType> : Action<ReturnType>, TgAction<ReturnType> {
    /**
     * The name of the field that will store the data.
     */
    val MediaAction<ReturnType>.dataField: String

    /**
     * Content type of media.
     */
    val MediaAction<ReturnType>.defaultType: MediaContentType

    /**
     * Media itself.
     */
    val MediaAction<ReturnType>.media: ImplicitFile<*>

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: String, via: TelegramBot) {
        internalSend(Recipient.String(to), via)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: Long, via: TelegramBot) {
        internalSend(Recipient.Long(to), via)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun send(to: User, via: TelegramBot) {
        internalSend(Recipient.Long(to.id), via)
    }

    /**
     * The internal method used to send the data.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun MediaAction<ReturnType>.internalSend(to: Recipient, via: TelegramBot) {
        parameters["chat_id"] = to.get()

        when (media) {
            is ImplicitFile.FromString -> {
                parameters[dataField] = media.file
                via.makeSilentRequest(method, parameters)
            }

            is ImplicitFile.FromByteArray -> via.makeSilentRequest(
                method,
                dataField,
                (parameters["file_name"]?.toString() ?: "$dataField.$defaultType"),
                media.file as ByteArray,
                parameters,
                defaultType.toContentType(),
            )

            is ImplicitFile.FromFile -> via.makeSilentRequest(
                method,
                dataField,
                (parameters["file_name"]?.toString() ?: (media.file as File).name),
                (media.file as File).readBytes(),
                parameters,
                withContext(Dispatchers.IO) {
                    ContentType.parse(
                        Files.probeContentType((media.file as File).toPath())
                            ?: return@withContext defaultType.toContentType(),
                    )
                },
            )
        }
    }

    /**
     * Make request with ability operating over response.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun sendAsync(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = internalSendAsync(returnType, Recipient.String(to), via)

    /**
     * Make request with ability operating over response.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun sendAsync(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = internalSendAsync(returnType, Recipient.Long(to), via)

    /**
     * Make request with ability operating over response.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    override suspend fun sendAsync(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = internalSendAsync(returnType, Recipient.Long(to.id), via)
}

/**
 * The internal method used to send the data with ability operating over response.
 *
 * @param returnType response type
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @return [Deferred]<[Response]<[ReturnType]>>
 */
internal suspend inline fun <R> MediaAction<R>.internalSendAsync(
    returnType: Class<R>,
    to: Recipient,
    via: TelegramBot,
): Deferred<Response<out R>> {
    parameters["chat_id"] = to.get()

    return when (media) {
        is ImplicitFile.FromString -> {
            parameters[dataField] = media.file
            via.makeRequestAsync(method, parameters, returnType, wrappedDataType)
        }

        is ImplicitFile.FromByteArray -> via.makeRequestAsync(
            method,
            dataField,
            (parameters["file_name"]?.toString() ?: "$dataField.$defaultType"),
            media.file as ByteArray,
            parameters,
            defaultType.toContentType(),
            returnType,
            wrappedDataType,
        )

        is ImplicitFile.FromFile -> via.makeRequestAsync(
            method,
            dataField,
            (parameters["file_name"]?.toString() ?: (media.file as File).name),
            (media.file as File).readBytes(),
            parameters,
            withContext(Dispatchers.IO) {
                ContentType.parse(
                    Files.probeContentType((media.file as File).toPath())
                        ?: return@withContext defaultType.toContentType(),
                )
            },
            returnType,
            wrappedDataType,
        )
    }
}
