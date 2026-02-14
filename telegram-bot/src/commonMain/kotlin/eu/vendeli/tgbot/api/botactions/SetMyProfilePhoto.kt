@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.InputProfilePhoto
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.transform

@TgAPI
class SetMyProfilePhotoAction(
    photo: InputProfilePhoto,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setMyProfilePhoto")
    override val method = "setMyProfilePhoto"
    override val returnType = getReturnType()
    override val beforeReq: () -> Unit = {
        val attachedContent = photo.file.transform(multipartData).file
        photo.file = attachedContent.toImplicitFile()
        parameters["photo"] = photo.encodeWith(InputProfilePhoto.serializer())
    }
}

/**
 * Changes the profile photo of the bot. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setmyprofilephoto)
 * @param photo The new profile photo to set
 * @returns [Boolean]
 */
@TgAPI
inline fun setMyProfilePhoto(photo: InputProfilePhoto) = SetMyProfilePhotoAction(photo)
