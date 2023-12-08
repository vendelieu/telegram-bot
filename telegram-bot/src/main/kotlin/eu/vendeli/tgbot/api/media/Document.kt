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
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendDocumentAction(document: ImplicitFile<*>) :
    MediaAction<Message>(),
    EntitiesContextBuilder,
    CaptionFeature<SendDocumentAction>,
    OptionsFeature<SendDocumentAction, DocumentOptions>,
    MarkupFeature<SendDocumentAction> {
    override val method = TgMethod("sendDocument")
    override val returnType = getReturnType()
    override val options = DocumentOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val inputFilePresence = document is ImplicitFile.InpFile

    init {
        parameters["document"] = document.file
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: ImplicitFile<*>) = SendDocumentAction(file)
inline fun document(block: () -> String) = document(ImplicitFile.Str(block()))

@Suppress("NOTHING_TO_INLINE")
inline fun document(ba: ByteArray) = document(ImplicitFile.InpFile(ba.toInputFile()))

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: File) = document(ImplicitFile.InpFile(file.toInputFile()))

@Suppress("NOTHING_TO_INLINE")
inline fun document(file: InputFile) = document(ImplicitFile.InpFile(file))

inline fun sendDocument(block: () -> String) = document(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendDocument(file: ImplicitFile<*>) = document(file)
