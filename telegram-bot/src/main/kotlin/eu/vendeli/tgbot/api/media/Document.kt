package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DocumentOptions

class SendDocumentAction :
    MediaAction<Message>,
    CaptionAble,
    OptionAble,
    MarkupAble,
    CaptionFeature<SendDocumentAction>,
    OptionsFeature<SendDocumentAction, DocumentOptions>,
    MarkupFeature<SendDocumentAction> {
    override val method: TgMethod = TgMethod("sendDocument")

    init {
        setDataField("document")
        setDefaultType(MediaContentType.Text)
    }

    constructor(documentId: String) {
        setId(documentId)
    }

    constructor(document: ByteArray) {
        setMedia(document)
    }

    override var options = DocumentOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun document(block: () -> String) = SendDocumentAction(block())

fun document(ba: ByteArray) = SendDocumentAction(ba)
