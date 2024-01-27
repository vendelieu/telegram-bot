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
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import java.io.File

class SendPhotoAction(photo: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendPhotoAction, PhotoOptions>,
    MarkupFeature<SendPhotoAction>,
    CaptionFeature<SendPhotoAction> {
    override val method = TgMethod("sendPhoto")
    override val returnType = getReturnType()
    override val options = PhotoOptions()

    init {
        handleImplicitFile(photo, "photo")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun photo(file: ImplicitFile) = SendPhotoAction(file)
inline fun photo(block: () -> String) = photo(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun photo(ba: ByteArray) = photo(ba.toImplicitFile("photo.jpg"))

@Suppress("NOTHING_TO_INLINE")
inline fun photo(file: File) = photo(file.toImplicitFile("photo.jpg"))

@Suppress("NOTHING_TO_INLINE")
inline fun photo(file: InputFile) = photo(file.toImplicitFile())

inline fun sendPhoto(block: () -> String) = photo(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendPhoto(file: ImplicitFile) = photo(file)
