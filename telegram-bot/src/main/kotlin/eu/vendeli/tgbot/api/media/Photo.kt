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
import eu.vendeli.tgbot.types.internal.options.PhotoOptions
import eu.vendeli.tgbot.types.internal.toInputFile
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import java.io.File

class SendPhotoAction(photo: ImplicitFile<*>) :
    MediaAction<Message>(),
    OptionsFeature<SendPhotoAction, PhotoOptions>,
    MarkupFeature<SendPhotoAction>,
    EntitiesContextBuilder,
    CaptionFeature<SendPhotoAction> {
    override val method = TgMethod("sendPhoto")
    override val returnType = getReturnType()
    override val OptionsFeature<SendPhotoAction, PhotoOptions>.options: PhotoOptions
        get() = PhotoOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"
    override val inputFilePresence = photo is ImplicitFile.InpFile

    init {
        parameters["photo"] = photo.file
    }
}

fun photo(block: () -> String) = SendPhotoAction(ImplicitFile.Str(block()))
fun photo(ba: ByteArray) = SendPhotoAction(ImplicitFile.InpFile(ba.toInputFile()))
fun photo(file: File) = SendPhotoAction(ImplicitFile.InpFile(file.toInputFile()))
fun photo(file: InputFile) = SendPhotoAction(ImplicitFile.InpFile(file))
