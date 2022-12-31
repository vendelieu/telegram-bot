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
import eu.vendeli.tgbot.types.internal.options.PhotoOptions
import java.io.File

class SendPhotoAction(private val photo: ImplicitFile<*>) :
    MediaAction<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<SendPhotoAction, PhotoOptions>,
    MarkupFeature<SendPhotoAction>,
    CaptionFeature<SendPhotoAction> {
    override val method: TgMethod = TgMethod("sendPhoto")
    override var options = PhotoOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    override val MediaAction<Message>.defaultType: MediaContentType
        get() = MediaContentType.ImageJpeg
    override val MediaAction<Message>.media: ImplicitFile<*>
        get() = photo
    override val MediaAction<Message>.dataField: String
        get() = "photo"
}

fun photo(block: () -> String) = SendPhotoAction(ImplicitFile.FromString(block()))

fun photo(ba: ByteArray) = SendPhotoAction(ImplicitFile.FromByteArray(ba))

fun photo(file: File) = SendPhotoAction(ImplicitFile.FromFile(file))
