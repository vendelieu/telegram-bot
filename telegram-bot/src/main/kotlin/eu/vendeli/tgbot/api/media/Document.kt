@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DocumentOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendDocumentAction(document: ImplicitFile) :
    MediaAction<Message>(),
    CaptionFeature<SendDocumentAction>,
    OptionsFeature<SendDocumentAction, DocumentOptions>,
    MarkupFeature<SendDocumentAction> {
    override val method = TgMethod("sendDocument")
    override val returnType = getReturnType()
    override val options = DocumentOptions()

    init {
        handleImplicitFile(document, "document")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: ImplicitFile) = SendDocumentAction(file)
inline fun document(block: () -> String) = document(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun document(ba: ByteArray) = document(ba.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: File) = document(file.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: InputFile) = document(file.toImplicitFile())

inline fun sendDocument(block: () -> String) = document(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendDocument(file: ImplicitFile) = document(file)
