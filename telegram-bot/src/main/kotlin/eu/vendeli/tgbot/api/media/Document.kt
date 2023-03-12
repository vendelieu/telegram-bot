@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.MediaContentType
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.DocumentOptions
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
    override val method: TgMethod = TgMethod("sendDocument")
    override val returnType = getReturnType()
    override var options = DocumentOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

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
