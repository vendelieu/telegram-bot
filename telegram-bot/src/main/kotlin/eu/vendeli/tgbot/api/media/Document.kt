package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DocumentOptions

class SendDocumentAction(document: ImplicitFile<*>) :
    MediaAction<Message>,
    CaptionAble,
    OptionAble,
    MarkupAble,
    CaptionFeature<SendDocumentAction>,
    OptionsFeature<SendDocumentAction, DocumentOptions>,
    MarkupFeature<SendDocumentAction> {
    override val method: TgMethod = TgMethod("sendDocument")

    init {
        when (document) {
            is ImplicitFile.FileId -> setId(document.file)
            is ImplicitFile.InputFile -> setMedia(document.file)
        }
        setDataField("document")
        setDefaultType(MediaContentType.Text)
    }

    override var options = DocumentOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun document(block: () -> String) = SendDocumentAction(ImplicitFile.FileId(block()))

fun document(ba: ByteArray) = SendDocumentAction(ImplicitFile.InputFile(ba))
