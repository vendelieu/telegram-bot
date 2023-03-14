@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.stickerset

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.StickerFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.toContentType
import eu.vendeli.tgbot.types.media.InputSticker
import eu.vendeli.tgbot.utils.builders.CreateNewStickerSetData
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.makeBunchMediaReq
import eu.vendeli.tgbot.utils.makeBunchMediaRequestAsync
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import kotlinx.coroutines.Deferred

class CreateNewStickerSetAction(
    private val data: CreateNewStickerSetData,
) : MediaAction<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("createNewStickerSet")
    override val returnType = getReturnType()
    private val allMediaString = data.stickers.all { it.sticker.file is ImplicitFile.FromString }
    private val files by lazy { mutableMapOf<String, ByteArray>() }

    override val MediaAction<Boolean>.defaultType: MediaContentType
        get() = data.stickers.first().sticker.contentType
    override val MediaAction<Boolean>.media: ImplicitFile<*>
        get() = TODO("Not implemented")
    override val MediaAction<Boolean>.dataField: String
        get() = TODO("Not implemented")

    init {
        val firstStickerFormat = data.stickers.first().sticker.stickerFormat
        require(data.stickers.all { it.sticker.stickerFormat == firstStickerFormat }) {
            "All stickers must be of the same type."
        }

        parameters["name"] = data.name
        parameters["title"] = data.title
        parameters["sticker_format"] = firstStickerFormat
        if (data.stickerType != null) parameters["sticker_type"] = data.stickerType
        if (data.needsRepainting != null) parameters["needs_repainting"] = data.needsRepainting

        parameters["stickers"] = if (allMediaString) data.stickers
        else data.stickers.mapIndexed { index, it ->
            val defaultName = "sticker-$index.$defaultType"
            InputSticker(
                sticker = it.sticker.let { s ->
                    // if string keep it as is
                    if (s.file is ImplicitFile.FromString) return@let s
                    // in other cases put file to special map
                    files[s.file.name ?: defaultName] = s.file.bytes

                    StickerFile.AttachedFile(
                        // and replace it with 'attach://$file' link
                        file = ImplicitFile.FromString("attach://${s.file.name ?: defaultName}"),
                        format = s.stickerFormat,
                        contentType = s.contentType,
                    )
                },
                emojiList = it.emojiList,
                maskPosition = it.maskPosition,
                keywords = it.keywords,
            )
        }
    }

    override suspend fun send(to: Long, via: TelegramBot) {
        parameters["chat_id"] = to
        internalSend(via)
    }

    override suspend fun send(to: User, via: TelegramBot) {
        parameters["chat_id"] = to.id
        internalSend(via)
    }

    override suspend fun send(to: String, via: TelegramBot) {
        parameters["chat_id"] = to
        internalSend(via)
    }

    override suspend fun sendAsync(to: Long, via: TelegramBot): Deferred<Response<out Boolean>> {
        parameters["chat_id"] = to
        return internalSendAsync(via)
    }

    override suspend fun sendAsync(to: User, via: TelegramBot): Deferred<Response<out Boolean>> {
        parameters["chat_id"] = to.id
        return internalSendAsync(via)
    }

    override suspend fun sendAsync(to: String, via: TelegramBot): Deferred<Response<out Boolean>> {
        parameters["chat_id"] = to
        return internalSendAsync(via)
    }

    private suspend inline fun internalSend(bot: TelegramBot) {
        if (allMediaString) bot.makeSilentRequest(method, parameters)
        else bot.makeBunchMediaReq(method, files, parameters, defaultType.toContentType())
    }

    private suspend inline fun internalSendAsync(bot: TelegramBot) =
        if (allMediaString) bot.makeRequestAsync(method, parameters, returnType, wrappedDataType)
        else bot.makeBunchMediaRequestAsync(
            method,
            files,
            parameters,
            defaultType.toContentType(),
            returnType,
            wrappedDataType,
        )
}

fun createNewStickerSet(block: CreateNewStickerSetData.() -> Unit) =
    CreateNewStickerSetAction(CreateNewStickerSetData("", "", listOf()).apply(block).validateFields())

fun createNewStickerSet(data: CreateNewStickerSetData) = CreateNewStickerSetAction(data)
