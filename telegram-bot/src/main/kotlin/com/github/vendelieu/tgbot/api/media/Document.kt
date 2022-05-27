package com.github.vendelieu.tgbot.api.media

import com.github.vendelieu.tgbot.interfaces.MediaAction
import com.github.vendelieu.tgbot.interfaces.features.*
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.DocumentOptions
import io.ktor.http.*

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
        setDefaultType(ContentType.Text.Plain)
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
