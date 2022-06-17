package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActionRecipientRef
import eu.vendeli.tgbot.types.internal.Response
import io.ktor.http.*
import kotlinx.coroutines.Deferred

/**
 * Media action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param Req_R response type.
 */
interface MediaAction<Req_R> : Action<Req_R>, TgAction {
    /**
     * A method that sets the name of the parameter that will contain the media data.
     *
     * @param dataField
     */
    fun MediaAction<Req_R>.setDataField(dataField: String) {
        Companion.dataField = dataField
    }

    /**
     * Set content type of media.
     *
     * @param defaultType
     */
    fun MediaAction<Req_R>.setDefaultType(defaultType: ContentType) {
        Companion.defaultType = defaultType
    }

    /**
     * Set file-id instead of sending media bytearray.
     *
     * @param id
     */
    fun MediaAction<Req_R>.setId(id: String) {
        Companion.id = id
    }

    /**
     * Set media in bytearray format.
     *
     * @param media
     */
    fun MediaAction<Req_R>.setMedia(media: ByteArray) {
        Companion.media = media
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    override suspend fun send(to: String, via: TelegramBot, isInline: Boolean) {
        internalSend(ActionRecipientRef.StringRecipient(to), via, isInline)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    override suspend fun send(to: Long, via: TelegramBot, isInline: Boolean) {
        internalSend(ActionRecipientRef.LongRecipient(to), via, isInline)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    override suspend fun send(to: User, via: TelegramBot, isInline: Boolean) {
        internalSend(ActionRecipientRef.LongRecipient(to.id), via, isInline)
    }

    /**
     * The internal method used to send the data.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    suspend fun MediaAction<Req_R>.internalSend(to: ActionRecipientRef, via: TelegramBot, isInline: Boolean) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to.get()

        if (id != null) {
            parameters[dataField] = id!!
            via.makeSilentRequest(method, parameters)
        } else via.makeSilentRequest(
            method,
            dataField,
            (parameters["file_name"]?.toString() ?: "$dataField.${defaultType.contentSubtype}"),
            media,
            parameters,
            defaultType
        )
    }

    /**
     * The internal method used to send the data with ability operating over response.
     *
     * @param returnType response type
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     * @return [Deferred]<[Response]<[Req_R]>>
     */
    suspend fun MediaAction<Req_R>.internalSendAsync(
        returnType: Class<Req_R>,
        to: ActionRecipientRef,
        via: TelegramBot,
        isInline: Boolean,
    ): Deferred<Response<Req_R>> {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to.get()

        return if (id != null) {
            parameters[dataField] = id!!
            via.makeRequestAsync(method, parameters, returnType, bunchResponseInnerType())
        } else via.makeRequestAsync(
            method,
            dataField,
            (parameters["file_name"]?.toString() ?: "$dataField.${defaultType.contentSubtype}"),
            media,
            parameters,
            defaultType,
            returnType,
            bunchResponseInnerType()
        )
    }

    companion object {
        var dataField: String = "media"
        var defaultType: ContentType = ContentType.Any
        var id: String? = null
        var media: ByteArray = ByteArray(0)
    }
}

/**
 * Make request with ability operating over response.
 *
 * @param Req_R
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @param isInline Whether the request is inline.
 * @return [Deferred]<[Response]<[Req_R]>>
 */
suspend inline fun <reified Req_R> MediaAction<Req_R>.sendAsync(
    to: String,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<Req_R>> =
    internalSendAsync(Req_R::class.java, ActionRecipientRef.StringRecipient(to), via, isInline)

/**
 * Make request with ability operating over response.
 *
 * @param Req_R
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @param isInline Whether the request is inline.
 * @return [Deferred]<[Response]<[Req_R]>>
 */
suspend inline fun <reified Req_R> MediaAction<Req_R>.sendAsync(
    to: Long,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<Req_R>> =
    internalSendAsync(Req_R::class.java, ActionRecipientRef.LongRecipient(to), via, isInline)

/**
 * Make request with ability operating over response.
 *
 * @param Req_R
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @param isInline Whether the request is inline.
 * @return [Deferred]<[Response]<[Req_R]>>
 */
suspend inline fun <reified Req_R> MediaAction<Req_R>.sendAsync(
    to: User,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<Req_R>> =
    internalSendAsync(Req_R::class.java, ActionRecipientRef.LongRecipient(to.id), via, isInline)
