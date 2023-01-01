@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionAble
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DocumentOptions
import java.io.File

class SendDocumentAction(private val document: ImplicitFile<*>) :
    MediaAction<Message>,
    CaptionAble,
    OptionAble,
    MarkupAble,
    CaptionFeature<SendDocumentAction>,
    OptionsFeature<SendDocumentAction, DocumentOptions>,
    MarkupFeature<SendDocumentAction> {
    override val method: TgMethod = TgMethod("sendDocument")
    override var options = DocumentOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.Text
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = document
    override val MediaAction<Message>.dataField: String
        get() = "document"
}

fun document(block: () -> String) = SendDocumentAction(ImplicitFile.FromString(block()))

fun document(ba: ByteArray) = SendDocumentAction(ImplicitFile.FromByteArray(ba))

fun document(file: File) = SendDocumentAction(ImplicitFile.FromFile(file))
