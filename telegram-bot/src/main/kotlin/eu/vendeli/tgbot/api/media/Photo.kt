package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PhotoOptions

class SendPhotoAction(photo: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendPhotoAction, PhotoOptions>,
    MarkupFeature<SendPhotoAction>,
    CaptionFeature<SendPhotoAction> {
    override val method: TgMethod = TgMethod("sendPhoto")

    init {
        when (photo) {
            is ImplicitFile.FileId -> setId(photo.file)
            is ImplicitFile.InputFile -> setMedia(photo.file)
        }
        setDataField("photo")
        setDefaultType(MediaContentType.ImageJpeg)
    }

    override var options = PhotoOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun photo(block: () -> String) = SendPhotoAction(ImplicitFile.FileId(block()))

fun photo(ba: ByteArray) = SendPhotoAction(ImplicitFile.InputFile(ba))
