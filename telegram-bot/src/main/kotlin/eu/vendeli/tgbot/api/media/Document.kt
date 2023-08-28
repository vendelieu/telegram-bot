@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.TgAction
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

class SendDocumentAction(private val document: ImplicitFile<*>) :
    MediaAction<Message>,
    ActionState(),
    EntitiesContextBuilder,
    CaptionFeature<SendDocumentAction>,
    OptionsFeature<SendDocumentAction, DocumentOptions>,
    MarkupFeature<SendDocumentAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendDocument")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendDocumentAction, DocumentOptions>.options: DocumentOptions
        get() = DocumentOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val MediaAction<Message>.inputFilePresence: Boolean
        get() = document is ImplicitFile.InpFile

    init {
        parameters["document"] = document.file
    }
}

fun document(block: () -> String) = SendDocumentAction(ImplicitFile.Str(block()))
fun document(ba: ByteArray) = SendDocumentAction(ImplicitFile.InpFile(ba.toInputFile()))
fun document(file: File) = SendDocumentAction(ImplicitFile.InpFile(file.toInputFile()))
fun document(file: InputFile) = SendDocumentAction(ImplicitFile.InpFile(file))
