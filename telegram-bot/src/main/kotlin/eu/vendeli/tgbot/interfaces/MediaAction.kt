package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Response
import io.ktor.http.*
import kotlinx.coroutines.Deferred

interface MediaAction<Req_R> : Action<Req_R>, TgAction {
    fun MediaAction<Req_R>.setDataField(dataField: String) {
        Companion.dataField = dataField
    }

    fun MediaAction<Req_R>.setDefaultType(defaultType: ContentType) {
        Companion.defaultType = defaultType
    }

    fun MediaAction<Req_R>.setId(id: String) {
        Companion.id = id
    }

    fun MediaAction<Req_R>.setMedia(media: ByteArray) {
        Companion.media = media
    }

    override suspend fun send(to: String, via: TelegramBot, isInline: Boolean) {
        internalSend(ActionRecipientRef.StringRecipient(to), via, isInline)
    }

    override suspend fun send(to: Long, via: TelegramBot, isInline: Boolean) {
        internalSend(ActionRecipientRef.LongRecipient(to), via, isInline)
    }

    override suspend fun send(to: User, via: TelegramBot, isInline: Boolean) {
        internalSend(ActionRecipientRef.LongRecipient(to.id), via, isInline)
    }

    suspend fun MediaAction<Req_R>.internalSend(to: ActionRecipientRef, via: TelegramBot, isInline: Boolean) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to

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

sealed class ActionRecipientRef {
    abstract fun get(): Any
    data class StringRecipient(val to: String) : ActionRecipientRef() {
        override fun get(): Any = to
    }

    data class LongRecipient(val to: Long) : ActionRecipientRef() {
        override fun get(): Any = to
    }
}

suspend inline fun <reified Req_R> MediaAction<Req_R>.sendAsync(
    to: String,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<Req_R>> =
    internalSendAsync(Req_R::class.java, ActionRecipientRef.StringRecipient(to), via, isInline)

suspend inline fun <reified Req_R> MediaAction<Req_R>.sendAsync(
    to: Long,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<Req_R>> =
    internalSendAsync(Req_R::class.java, ActionRecipientRef.LongRecipient(to), via, isInline)

suspend inline fun <reified Req_R> MediaAction<Req_R>.sendAsync(
    to: User,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<Req_R>> =
    internalSendAsync(Req_R::class.java, ActionRecipientRef.LongRecipient(to.id), via, isInline)
