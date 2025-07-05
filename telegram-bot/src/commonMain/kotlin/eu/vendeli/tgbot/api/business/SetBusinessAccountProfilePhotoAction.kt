@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.types.media.InputProfilePhoto
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import eu.vendeli.tgbot.utils.internal.transform

@TgAPI
class SetBusinessAccountProfilePhotoAction(
    businessConnectionId: String,
    photo: InputProfilePhoto,
    isPublic: Boolean? = null,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("setBusinessAccountProfilePhoto")
    override val method = "setBusinessAccountProfilePhoto"
    override val returnType = getReturnType()
    override val beforeReq: () -> Unit = {
        val attachedContent = photo.file.transform(multipartData).file
        photo.file = attachedContent.toImplicitFile()
        parameters["photo"] = photo.encodeWith(InputProfilePhoto.serializer())
    }

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        isPublic?.let { parameters["is_public"] = it.toJsonElement() }
    }
}

/**
 * Changes the profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setbusinessaccountprofilephoto)
 * @param businessConnectionId Unique identifier of the business connection
 * @param photo The new profile photo to set
 * @param isPublic Pass True to set the public photo, which will be visible even if the main photo is hidden by the business account's privacy settings. An account can have only one public photo.
 * @returns [Boolean]
 */
@TgAPI
inline fun setBusinessAccountProfilePhoto(
    businessConnectionId: String,
    photo: InputProfilePhoto,
    isPublic: Boolean? = null,
) = SetBusinessAccountProfilePhotoAction(businessConnectionId, photo, isPublic)
